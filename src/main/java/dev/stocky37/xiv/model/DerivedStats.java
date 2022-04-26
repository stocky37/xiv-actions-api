package dev.stocky37.xiv.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public record DerivedStats(Stats stats, Attribute primaryStat, LevelMod mod) {

	// level 90 level modifiers
	// source: https://www.akhmorning.com/allagan-studies/modifiers/levelmods/
	public static final LevelMod LVL90 = new LevelMod(390, 400, 1900);
	public static final BigDecimal DH_MULTIPLIER = BigDecimal.valueOf(1.25);

	public DerivedStats(Stats stats, Attribute primaryStat) {
		this(stats, primaryStat, LVL90);
	}

	public BigDecimal critChance() {
		final double cr = 200 * (stats.crit() - mod.sub()) / mod.div() + 50;
		return scale(BigDecimal.valueOf(cr));
	}

	public BigDecimal critDamage() {
		final double cd = 200 * (stats.crit() - mod.sub()) / mod.div() + 1400;
		return scale(BigDecimal.valueOf(cd));
	}

	public BigDecimal directHitChance() {
		final double dh = 550 * (stats.directHit() - mod.sub()) / mod.div();
		return scale(BigDecimal.valueOf(dh));
	}

	public int attackPower() {
		return switch(primaryStat) {
			case STRENGTH -> stats.strength();
			case DEXTERITY -> stats.dexterity();
			case MIND -> stats.mind();
			case INTELLIGENCE -> stats.intelligence();
			default -> throw new IllegalStateException("Unhandled primary stat: " + primaryStat());
		};
	}

	public int attackSpeed() {
		return switch(primaryStat) {
			case STRENGTH, DEXTERITY -> stats.skillSpeed();
			case MIND, INTELLIGENCE -> stats.spellSpeed();
			default -> throw new IllegalStateException("Unhandled primary stat: " + primaryStat());
		};
	}

//	public BigDecimal averageCritMultiplier() {
//		return averageExpectedMultiplier(critChance(), critDamage());
//	}
//
//	public BigDecimal averageDirectHitMultiplier() {
//		return averageExpectedMultiplier(directHitChance(), DH_MULTIPLIER);
//	}

//	private BigDecimal averageExpectedMultiplier(BigDecimal rate, BigDecimal multiplier) {
//		return rate.multiply(multiplier.subtract(BigDecimal.ONE)).add(BigDecimal.ONE);
//	}

//	public BigDecimal determinationMultiplier() {
//		final double det = 140 * (stats.determination() - mod.main()) / mod.div() + 1000;
//		return scale(BigDecimal.valueOf(det));
//	}
//
//	public BigDecimal autoAttackMultiplier() {
//		final double aa = 130 * (stats.speed() - mod.sub()) / mod.div() + 1000;
//		return scale(BigDecimal.valueOf(aa));
//	}

	private BigDecimal scale(BigDecimal num) {
		return num.movePointLeft(3).setScale(3, RoundingMode.FLOOR);
	}

	public Duration gcd(Duration baseGcd) {
		return Duration.ofMillis(
			BigDecimal.valueOf(baseGcd.toMillis() * gcdModifier())
				.setScale(-1, RoundingMode.FLOOR)
				.longValue()
		);
	}

//	public BigDecimal tenacityMultiplier() {
//		final double ten = 100 * (stats.tenacity() - mod.sub()) / mod.div() + 1000;
//		return scale(BigDecimal.valueOf(ten));
//	}

	private double gcdModifier() {
		final double gcdMod = Math.ceil(130 * (mod.sub() - attackSpeed()) / mod.div()) + 1000;
		return gcdMod / 1000;
	}

	public record LevelMod(int main, int sub, float div) {}

}
