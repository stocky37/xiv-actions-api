package dev.stocky37.xiv.core;

import static dev.stocky37.xiv.util.Util.floor;

import dev.stocky37.xiv.model.DamageModifier;
import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.StatModifier;
import dev.stocky37.xiv.model.Stats;
import dev.stocky37.xiv.model.Status;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public record DamageCalculator(Job job, DerivedStats stats) {
	public double expectedDamage(int potency) {
		return expectedDamage(potency, Collections.emptyList());
	}

	public double expectedDamage(int potency, Collection<Status> statusEffects) {
		if(potency == 0) {
			return 0;
		}
		final var stats = modifiedStats(statusEffects);
		final int d1 = floor(potency * stats.fatk() / 100d * stats.fdet());
		final int d2 = floor(d1 * stats.fwd() / 100d);
		return d2 * multipliers(stats, statusEffects);
	}

	public double expectedAutoDamage() {
		return expectedAutoDamage(Collections.emptyList());
	}

	public double expectedAutoDamage(Collection<Status> statusEffects) {
		final var stats = modifiedStats(statusEffects);
		final double d1 = 90 * stats.fatk() * stats.fdet() / 100;
		final double d2 = floor(d1 * stats.fspeed() / 1000d * stats.fauto() / 100d);
		return d2 * multipliers(stats, statusEffects);
	}

	public DerivedStats modifiedStats(Collection<Status> statusEffects) {
		var newStats = this.stats.baseStats();
		for(final var effect : statModifiers(statusEffects).toList()) {
			newStats = effect.apply(newStats);
		}
		return new DerivedStats(newStats, this.stats.primaryStat(), this.stats.mod());
	}

	private static double multipliers(DerivedStats stats, Collection<Status> statusEffects) {
		var multipliers = averageCritMultiplier(stats) * averageDirectHitMultiplier(stats);
		for(var mod : damageModifiers(statusEffects).toList()) {
			multipliers = mod.apply(multipliers);
		}
		return multipliers;
	}

	private static double averageCritMultiplier(DerivedStats stats) {
		return averageExpectedMultiplier(stats.critChance(), stats.critDamage());
	}

	private static double averageDirectHitMultiplier(DerivedStats stats) {
		return averageExpectedMultiplier(stats.directHitChance(), stats.directHitDamage());
	}

	private static double averageExpectedMultiplier(double rate, double multiplier) {
		return rate * (multiplier - 1) + 1;
	}

	private static Stream<Status.Effect<Stats>> statModifiers(Collection<Status> statusEffects) {
		return statusEffects.stream()
			.flatMap(status -> status.effects().stream())
			.filter(effect -> effect.type() == Status.Type.STAT)
			.map(StatModifier.class::cast);
	}

	private static Stream<Status.Effect<Double>> damageModifiers(Collection<Status> statusEffects) {
		return statusEffects.stream()
			.flatMap(status -> status.effects().stream())
			.filter(effect -> effect.type() == Status.Type.DAMAGE)
			.map(DamageModifier.class::cast);
	}

}
