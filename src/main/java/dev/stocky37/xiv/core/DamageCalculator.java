package dev.stocky37.xiv.core;

import static dev.stocky37.xiv.util.Util.floor;
import static dev.stocky37.xiv.util.Util.scale;

import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.RotationAction;
import java.util.function.Function;

public record DamageCalculator(Job job, DerivedStats stats) implements
	Function<RotationAction, Integer> {

	// todo: modifiers can be found in the following fields from XIVAPI ClassJob endpoint
	//  - ModifierDexterity
	//  - ModifierHitPoints
	//  - ModifierIntelligence
	//  - ModifierManaPoints
	//  - ModifierMind
	//  - ModifierPiety
	//  - ModifierStrength
	//  - ModifierVitality
	private static final int JOB_MODIFIER = 115;

	@Override
	public Integer apply(RotationAction rotationAction) {
		return (int) Math.floor(expectedDamage(rotationAction.action().potency()));
	}

	public double expectedDamage(int potency) {
		if(potency == 0) {
			return 0;
		}
		final int d1 = floor(potency * fatk() / 100d * fdet());
		final int d2 = floor(d1 * fwd() / 100d);
		return d2 * averageCritMultiplier() * averageDirectHitMultiplier();
	}

	public int fatk() {
		return floor(
			stats.mod().fatk()
				* (stats.attackPower() - stats.mod().main()) / (double) stats.mod().main()
				+ 100
		);
	}

	public int fwd() {
		return floor(stats.mod().main() * JOB_MODIFIER / 1000d + stats.weaponDamage());
	}

	public double fdet() {
		return scale(140
			* (stats.stats().determination() - stats.mod().main()) / (double) stats.mod().div()
			+ 1000
		);
	}

	public double faa() {
		return scale(130
			* (stats.attackSpeed() - stats.mod().sub()) / (double) stats.mod().div()
			+ 1000
		);
	}

	private double averageCritMultiplier() {
		return averageExpectedMultiplier(stats.critChance(), stats.critDamage());
	}

	private double averageDirectHitMultiplier() {
		return averageExpectedMultiplier(stats.directHitChance(), stats.directHitDamage());
	}

	private double averageExpectedMultiplier(double rate, double multiplier) {
		return rate * (multiplier - 1) + 1;
	}
}
