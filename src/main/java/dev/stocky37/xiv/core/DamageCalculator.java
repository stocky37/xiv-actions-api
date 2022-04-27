package dev.stocky37.xiv.core;

import static dev.stocky37.xiv.util.Util.floor;

import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.Job;
import java.util.function.Function;

public record DamageCalculator(Job job, DerivedStats stats) implements Function<Action, Double> {

	@Override
	public Double apply(Action action) {
		return expectedDamage(action.potency());
	}

	public double expectedDamage(int potency) {
		if(potency == 0) {
			return 0;
		}
		final int d1 = floor(potency * stats.fatk() / 100d * stats.fdet());
		final int d2 = floor(d1 * stats.fwd() / 100d);
		return d2 * multipliers();
	}

	public double expectedAutoDamage() {
		final double d1 = 90 * stats.fatk() * stats.fdet() / 100;
		final double d2 = floor(d1 * stats.fspeed() / 1000d * stats.fauto() / 100d);
		return d2 * multipliers();
	}

	public double averageCritMultiplier() {
		return averageExpectedMultiplier(stats.critChance(), stats.critDamage());
	}

	public double averageDirectHitMultiplier() {
		return averageExpectedMultiplier(stats.directHitChance(), stats.directHitDamage());
	}

	private double averageExpectedMultiplier(double rate, double multiplier) {
		return rate * (multiplier - 1) + 1;
	}

	private double multipliers() {
		return averageCritMultiplier() * averageDirectHitMultiplier();
	}
}
