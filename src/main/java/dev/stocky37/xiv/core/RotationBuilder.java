package dev.stocky37.xiv.core;

import com.google.common.collect.Lists;
import dev.stocky37.xiv.config.XivConfig;
import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.Rotation;
import dev.stocky37.xiv.model.RotationAction;
import dev.stocky37.xiv.model.RotationEffect;
import dev.stocky37.xiv.model.Stats;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RotationBuilder {

	private final Duration BASE_GCD = Duration.ofMillis(2500);
	private final XivConfig config;
	private final List<Action> actions = new ArrayList<>();
	private final List<RotationEffect> rotationEffects = new ArrayList<>();
	private Duration nextGcd = Duration.ZERO;
	private Duration nextOGcd = Duration.ZERO;
	private int gcdCount = 0;
	private final Stats baseStats;
	private final Job job;

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

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public Rotation build() {
		final List<RotationAction> timeline = actions.stream().map(this::buildAction).toList();
		final double totalDamage =
			timeline.stream().map(RotationAction::damage).reduce(Double::sum).get();
		final Duration totalTime = timeline.get(timeline.size() - 1).timestamp();

		return new Rotation(timeline, totalDamage / totalTime.toSeconds());
	}

	private RotationAction buildAction(Action action) {
		final DerivedStats stats = new DerivedStats(baseStats, job.primaryStat().orElseThrow());
		final DamageCalculator calc =
			new DamageCalculator(job, stats);
		final double damage = calc.expectedDamage(action.potency());
		return action.onGCD() ? handleGcd(action, damage, stats) : handleOGcd(action, damage);
	}

	private RotationAction handleGcd(Action action, double damage, DerivedStats stats) {
		removeEffects(nextGcd);
		addEffects(action, nextGcd);

		final var rotationAction = new RotationAction(
			action,
			nextGcd,
			Optional.of(++gcdCount),
			Lists.newArrayList(rotationEffects),
			damage
		);

		nextOGcd = nextGcd.plus(animationLock(action));
		nextGcd = nextGcd.plus(calcGcdCooldown(action, stats.gcd(BASE_GCD)));

		return rotationAction;
	}

	private Duration calcGcdCooldown(Action action, Duration gcd) {
		return recastScales(action) ? gcd : action.recast();
	}

	// todo: need a much better way to figure this out
	//       if needed can manually add a field
	// assuming only 2500 gcd skills are affected by skill/spell speed
	// there may be a better way of checking, but this appears to work for now
	// also need to check if there is a separate cooldown group - if there is,
	// assume standard gcd length
	private boolean recastScales(Action action) {
		return action.recast().equals(Duration.ofMillis(2500)) || !action.cooldownGroups().isEmpty();
	}

	private RotationAction handleOGcd(Action action, double damage) {
		removeEffects(nextOGcd);
		addEffects(action, nextOGcd);

		final var rotationAction = new RotationAction(
			action,
			nextOGcd,
			Optional.empty(),
			Lists.newArrayList(rotationEffects),
			damage
		);

		nextOGcd = nextOGcd.plus(animationLock(action));
		if(nextOGcd.compareTo(nextGcd) > 0) {
			nextGcd = nextOGcd;
		}
		return rotationAction;
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
