package dev.stocky37.xiv.core;

import com.google.common.collect.Lists;
import dev.stocky37.xiv.config.XivConfig;
import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.model.ActiveStatus;
import dev.stocky37.xiv.model.AutoAttackEvent;
import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.GcdEvent;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.OGcdEvent;
import dev.stocky37.xiv.model.Rotation;
import dev.stocky37.xiv.model.Stats;
import dev.stocky37.xiv.model.Status;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang3.ObjectUtils;

public class RotationBuilder {

	private final Duration BASE_GCD = Duration.ofMillis(2500);
	private final XivConfig config;
	private final List<Action> actions = new ArrayList<>();
	private final Map<String, ActiveStatus> statusEffects = new LinkedHashMap<>();
	private Duration nextGcd = Duration.ZERO;
	private Duration nextOGcd = Duration.ZERO;
	private int gcdCount = 0;
	private final Stats baseStats;
	private final Job job;

	private final List<Long> autos = new ArrayList<>();

	private final DamageCalculator calc;

	public RotationBuilder(XivConfig config, Job job, Stats baseStats) {
		this.config = config;
		this.baseStats = baseStats;
		this.job = job;
		this.calc = new DamageCalculator(job, new DerivedStats(baseStats, job.primaryStat().orElseThrow()));
	}

	public RotationBuilder append(Action action) {
		this.actions.add(action);
		return this;
	}

	public RotationBuilder appendActions(List<Action> actions) {
		this.actions.addAll(actions);
		return this;
	}

	public Rotation build() {
		final var actionTimeline = new Timeline();
		final var autosTimeline = new Timeline();
		actions.stream().map(this::handleAction).forEach(actionTimeline::addEvent);
		handleAutos(actionTimeline).forEach(autosTimeline::addEvent);


		final long totalDamage = Stream.concat(actionTimeline.values().stream(), autosTimeline.values().stream())
			.map(Timeline.Event::damage)
			.reduce(Long::sum)
			.orElse(0L);
		final double dps = totalDamage / (double) actionTimeline.lastKey().toMillis() * 1000;

		return new Rotation(actionTimeline.values(), autosTimeline.values(), dps);
	}

	private Timeline.Event handleAction(Action action) {
		return action.onGCD() ? handleGcd(action) : handleOGcd(action);
	}

	private Timeline.Event handleGcd(Action action) {
		removeStatusEffects(nextGcd);
		addStatusEffects(action, nextGcd);
		final var statusEffects = Lists.newArrayList(this.statusEffects.values());

		final var event = new GcdEvent(
			nextGcd,
			action,
			(long) calc.expectedDamage(action.potency(), statusEffects.stream().map(ActiveStatus::status).toList()),
			++gcdCount,
			statusEffects
		);

		nextOGcd = nextGcd.plus(animationLock(action));
		nextGcd = nextGcd.plus(calcGcdCooldown(action, stats().gcd(BASE_GCD)));

		return event;
	}

	private Timeline.Event handleOGcd(Action action) {
		removeStatusEffects(nextGcd);
		addStatusEffects(action, nextGcd);
		final var statusEffects = Lists.newArrayList(this.statusEffects.values());
		final var event = new OGcdEvent(
			nextOGcd,
			action,
			(long) calc.expectedDamage(action.potency()),
			statusEffects
		);

		nextOGcd = nextOGcd.plus(animationLock(action));
		if(nextOGcd.compareTo(nextGcd) > 0) {
			nextGcd = nextOGcd;
		}
		return event;
	}

	private DerivedStats stats() {
		return new DerivedStats(currentBaseStats(), job.primaryStat().orElseThrow());
	}

	//todo: modify with any status effects that need updating
	private Stats currentBaseStats() {
		return baseStats;
	}

	private Collection<Timeline.Event> handleAutos(Timeline actionTimeline) {
		final var endTime = actionTimeline.lastKey();
		final List<Timeline.Event> autos = new ArrayList<>();
		for(long i = 0; i < endTime.toMillis(); i += currentBaseStats().delayDuration().toMillis()) {
			final var timestamp = Duration.ofMillis(i);

			// note: since lowerEntry gets keys strrictly BEFORE the current timestamp, this has the
			//       effect of assuming autos occur before actions at the same timestamp
			final var entry = actionTimeline.lowerEntry(timestamp);

			// check the status effects in effect after the last action
			// remove any that have fallen off by now
			if(entry != null) {
				final var statusEffects = Lists.newArrayList(entry.getValue().statusEffects());
				remvoeStatusEffects(timestamp, statusEffects);
				autos.add(new AutoAttackEvent(
					timestamp,
					(long) calc.expectedAutoDamage(statusEffects.stream().map(ActiveStatus::status).toList()),
					statusEffects
				));
			} else {
				autos.add(new AutoAttackEvent(timestamp, (long) calc.expectedAutoDamage(), Collections.emptyList()));
			}
		}
		return autos;
	}

	private Duration calcGcdCooldown(Action action, Duration gcd) {
		return shouldGcdScale(action) ? gcd : action.recast();
	}

	// todo: need a much better way to figure this out
	//       if needed can manually add a field
	// assuming only 2500 gcd skills are affected by skill/spell speed
	// there may be a better way of checking, but this appears to work for now
	// also need to check if there is a separate cooldown group - if there is,
	// assume standard gcd length
	private boolean shouldGcdScale(Action action) {
		return action.recast().equals(Duration.ofMillis(2500)) || !action.cooldownGroups().isEmpty();
	}

	private Duration animationLock(Action action) {
		return action.animationLock().orElse(config.animationLock()).plus(config.ping());
	}

	private void addStatusEffects(Action action, Duration now) {
		action.statusEffects()
			.forEach(status -> statusEffects.put(
				status.id(),
				newStatus(status, now)
			));
	}

	private void removeStatusEffects(Duration now) {
		statusEffects.entrySet().removeIf(e -> e.getValue().end().compareTo(now) <= 0);
	}

	private static void remvoeStatusEffects(Duration now, Collection<ActiveStatus> statusEffects) {
		statusEffects.removeIf(e -> e.end().compareTo(now) <= 0);
	}

	private ActiveStatus newStatus(Status status, Duration now) {
		// if status has a maxduration it can be refreshed up to the max duration
		if(status.maxDuration().isPresent() && statusEffects.containsKey(status.id())) {
			final ActiveStatus existing = statusEffects.get(status.id());
			// now:       0
			// start:  -> 0
			// (left): -> 30 (end - now)
			// (max):  -> 60 (start + 60)
			// end:    -> 30

			// now:          1
			// start:   0 -> 1
			// (left):    -> 29 (end[30] - now[1])
			// (max):     -> 61 (start + 60)
			// end:    30 -> 60 (min(end + end, start + maxDuration))

			// now:          2
			// start:   1 -> 2
			// (left):    -> 58 (end[60] - now[2])
			// (max):     -> 62 (start[2] + 60)
			// end:       -> 62 min(end + end[92], max[62])

			final Duration maxEnd = now.plus(status.maxDuration().get());
			final Duration proposedEnd = existing.end().plus(status.duration());
			final Duration newEnd = ObjectUtils.min(maxEnd, proposedEnd);
			return new ActiveStatus(status, now, newEnd);
		} else {
			// otherwise it should be fine just to replace with a new instance
			return new ActiveStatus(status, now, now.plus(status.duration()));
		}

	}


}
