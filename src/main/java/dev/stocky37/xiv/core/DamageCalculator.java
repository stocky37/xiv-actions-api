package dev.stocky37.xiv.core;

import static dev.stocky37.xiv.util.Util.floor;

import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.Job;
import java.util.function.Function;

public record DamageCalculator(Job job, DerivedStats stats) implements
	Function<Action, Double> {

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
		return d2 * averageCritMultiplier() * averageDirectHitMultiplier();
	}


//	public double faa() {
//		return scale(130
//			* (stats.attackSpeed() - stats.mod().sub()) / (double) stats.mod().div()
//			+ 1000
//		);
//	}
//
//	public double fauto() {
//		return floor(
//			(Util.JOB_MODIFIER * stats.mod().main() / 1000d)
//				+ stats.weaponDamage() * stats().stats().delay() / 3
//		);
//	}

	public double averageCritMultiplier() {
		return averageExpectedMultiplier(stats.critChance(), stats.critDamage());
	}

	public double averageDirectHitMultiplier() {
		return averageExpectedMultiplier(stats.directHitChance(), stats.directHitDamage());
	}

	private double averageExpectedMultiplier(double rate, double multiplier) {
		return rate * (multiplier - 1) + 1;
	}
}
