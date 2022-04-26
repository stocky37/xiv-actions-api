package dev.stocky37.xiv.core;

import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.RotationAction;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

	public double calcDamage(int potency) {
		final int d1 = floor(potency * fatk() / 100d * stats.determinationMultiplier().doubleValue());
		final int d2 = floor(d1 * fwd() / 100d);

		System.out.println("d1: " + d1);
		System.out.println("d2: " + d2);

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

	@Override
	public Integer apply(RotationAction rotationAction) {
		return (int) Math.floor(calcDamage(rotationAction.action().potency()));
	}

	private double averageCritMultiplier() {
		return averageExpectedMultiplier(stats.critChance(), stats.critDamage());
	}

	private double averageDirectHitMultiplier() {
		return averageExpectedMultiplier(stats.directHitChance(), stats.directHitDamage());
	}

	private double averageExpectedMultiplier(BigDecimal rate, BigDecimal multiplier) {
		return rate.doubleValue() * (multiplier.doubleValue() - 1) + 1;
	}

	private int floor(double val) {
		return BigDecimal.valueOf(val).setScale(0, RoundingMode.FLOOR).intValue();
	}
}
