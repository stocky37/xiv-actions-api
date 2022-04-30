package dev.stocky37.xiv.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.stocky37.xiv.config.XivConfig;
import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.model.ActiveStatus;
import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.Job;
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
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.apache.commons.lang3.ObjectUtils;

public class RotationBuilder {
	private final Duration BASE_GCD = Duration.ofMillis(2500);
	private final XivConfig config;
	private final List<Action> actions = new ArrayList<>();
	private final Map<String, ActiveStatus> statusEffects = new LinkedHashMap<>();
	private Duration nextGcd = Duration.ZERO;
	private Duration nextOGcd = Duration.ZERO;
	private final Stats baseStats;
	private final DamageCalculator calc;
	private final Timeline timeline = new Timeline();


	public RotationBuilder(XivConfig config, Job job, Stats baseStats) {
		this.config = config;
		this.baseStats = baseStats;
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

		final var autosTimeline = new Timeline();
		actions.stream()
			.map(a -> handleAction(a, timeline))
			.filter(Objects::nonNull)
			.forEach(timeline::addEvent);

		handleAutos(timeline).forEach(timeline::addEvent);

		return new Rotation(timeline.values(), timeline.dps());
	}

	private Timeline.Event handleAction(Action action, Timeline timeline) {
		return action.actionType() == Action.Type.DELAY ? handleDelay(action, timeline) : handleAbility(action);
	}

	private Timeline.Event handleDelay(Action action, Timeline timeline) {
		final var lastAction = timeline.lastAction();
		nextOGcd = lastAction.getKey().plus(action.animationLock().orElseThrow());
		nextGcd = ObjectUtils.max(nextGcd, nextOGcd);
		return null;
	}

	private Timeline.Event handleAbility(Action action) {
		final var timestamp = action.onGCD() ? nextGcd : nextOGcd;

		removeStatusEffects(timestamp);

		// need to copy the list to not propagate changes to the original collection
		final var activeStatusEffects = this.statusEffects.values().stream().map(ActiveStatus::status).toList();
		final long damage = (long) calc.expectedDamage(action.potency(), activeStatusEffects);

		addStatusEffects(action, nextGcd);

		final var event = Timeline.actionEvent(
			timestamp,
			damage,
			// need to copy the list to not propagate changes to the original collection
			// need to get a separate copy fron above in order to get the effects added by this action
			Lists.newArrayList(this.statusEffects.values()),
			action
		);

		nextOGcd = timestamp.plus(animationLock(action));
		nextGcd = action.onGCD() ? nextGcd.plus(gcd(action, activeStatusEffects)) : ObjectUtils.max(nextGcd, nextOGcd);

		return event;
	}

	private Duration gcd(Action action, List<Status> statusEffects) {
		// assuming that if there are separate cooldowns attached that it should it should be treated as a standard gcd
		final var baseGcd = action.cooldownGroups().isEmpty() ? action.recast() : BASE_GCD;

		// assuming only 2500ms gcds scale - will need to revisit if assumption if false
		// if false, may need to extrapolate some other way, or manually enrich data
		return baseGcd.equals(BASE_GCD) ? calc.modifiedStats(statusEffects).gcd(baseGcd) : baseGcd;
	}

	//todo: modify with any statusEffects effects that need updating
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

			// check the statusEffects effects in effect after the last action
			// remove any that have fallen off by now
			if(entry != null) {
				final var statusEffects = Lists.newArrayList(entry.getValue().statusEffects());
				remvoeStatusEffects(timestamp, statusEffects);
				autos.add(Timeline.autoAttackEvent(
					timestamp,
					(long) calc.expectedAutoDamage(statusEffects.stream().map(ActiveStatus::status).toList()),
					statusEffects
				));
			} else {
				autos.add(Timeline.autoAttackEvent(
					timestamp,
					(long) calc.expectedAutoDamage(),
					Collections.emptyList()
				));
			}
		}
		return autos;
	}

	private Duration animationLock(Action action) {
		return (action.animationLock().orElse(config.animationLock())).plus(config.ping());
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
		// if statusEffects has a maxduration it can be refreshed up to the max duration
		if(status.maxDuration().isPresent() && statusEffects.containsKey(status.id())) {
			final ActiveStatus existing = statusEffects.get(status.id());
			final Duration maxEnd = now.plus(status.maxDuration().get());
			final Duration newEnd = existing.end().plus(status.duration());
			return new ActiveStatus(status, now, ObjectUtils.min(maxEnd, newEnd));
		} else {
			// otherwise it should be fine just to replace with a new instance
			return new ActiveStatus(status, now, now.plus(status.duration()));
		}
	}
}
