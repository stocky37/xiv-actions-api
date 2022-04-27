package dev.stocky37.xiv.core;

import dev.stocky37.xiv.config.XivConfig;
import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.GcdEvent;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.OGcdEvent;
import dev.stocky37.xiv.model.Rotation;
import dev.stocky37.xiv.model.RotationEffect;
import dev.stocky37.xiv.model.Stats;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class RotationBuilder {

	private final Duration BASE_GCD = Duration.ofMillis(2500);
	private final XivConfig config;
	private final List<Action> actions = new ArrayList<>();
	private final List<RotationEffect> rotationEffects = new ArrayList<>();
	private Duration nextGcd = Duration.ZERO;
	private Duration nextOGcd = Duration.ZERO;

	private Duration nextAuto = Duration.ZERO;
	private int gcdCount = 0;
	private final Stats baseStats;
	private final Job job;

	private final List<Long> autos = new ArrayList<>();

	public RotationBuilder(XivConfig config, Job job, Stats baseStats) {
		this.config = config;
		this.baseStats = baseStats;
		this.job = job;
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
		final var timeline = new Timeline();
		actions.stream().map(this::handleAction).forEach(timeline::addEvent);

		final long totalDamage = timeline.values().stream().map(Timeline.Event::damage).reduce(Long::sum).orElse(0L);
		final double dps = totalDamage / (double) timeline.lastKey().toMillis() * 1000;

		return new Rotation(timeline.values(), dps);
	}

	private Timeline.Event handleAction(Action action) {
		return action.onGCD() ? handleGcd(action) : handleOGcd(action);
	}

	private Timeline.Event handleGcd(Action action) {
		removeEffects(nextGcd);
		final DamageCalculator calc = calculator();
		addEffects(action, nextGcd);

		final var event = new GcdEvent(nextGcd, action, (long) calc.expectedDamage(action.potency()), ++gcdCount);

		nextOGcd = nextGcd.plus(animationLock(action));
		nextGcd = nextGcd.plus(calcGcdCooldown(action, stats().gcd(BASE_GCD)));

		return event;
	}

	private Timeline.Event handleOGcd(Action action) {
		removeEffects(nextOGcd);
		final DamageCalculator calc = calculator();
		addEffects(action, nextOGcd);

		final var event = new OGcdEvent(nextOGcd, action, (long) calc.expectedDamage(action.potency()));
		nextOGcd = nextOGcd.plus(animationLock(action));
		if(nextOGcd.compareTo(nextGcd) > 0) {
			nextGcd = nextOGcd;
		}
		return event;
	}

	private DamageCalculator calculator() {
		return new DamageCalculator(job, stats());
	}

	private DerivedStats stats() {
		return new DerivedStats(currentBaseStats(), job.primaryStat().orElseThrow());
	}

	//todo: modify with any status effects that need updating
	private Stats currentBaseStats() {
		return baseStats;
	}

	private void handleAutos(Duration timestamp) {
		final Duration autoDelay = currentBaseStats().delayDuration();
		if(nextAuto.compareTo(timestamp) < 0) {
			removeEffects(nextAuto);
			final DamageCalculator calc = calculator();
			autos.add((long) calc.expectedAutoDamage());
			nextAuto = nextAuto.plus(autoDelay);
		}
	}

	;

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

	private void addEffects(Action action, Duration start) {
		action.effects().forEach(e -> rotationEffects.add(new RotationEffect(
			e,
			start,
			start.plus(e.length())
		)));
	}

	private void removeEffects(Duration start) {
		final List<RotationEffect> finishedEffects = new ArrayList<>();
		rotationEffects.stream()
			.filter(item -> item.end().compareTo(start) < 0)
			.forEach(finishedEffects::add);
		rotationEffects.removeAll(finishedEffects);
	}
}
