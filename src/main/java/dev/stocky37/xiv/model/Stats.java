package dev.stocky37.xiv.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public record Stats(
	int attackPower,
	int weaponDamage,
	int delay,
	int attackSpeed,
	int tenacity,
	int directHit,
	int crit,
	int determination,
	int speed
) {
	// level 90 level modifiers
	// source: https://www.akhmorning.com/allagan-studies/modifiers/levelmods/
	public static final int MAIN = 390;
	public static final int SUB = 400;
	public static final float DIV = 1900;

	public static final BigDecimal DH_MULTIPLIER = BigDecimal.valueOf(1.25);

	public static Builder builder() {
		return new Builder();
	}

	public BigDecimal critChance() {
		final double cr = 200 * (crit - SUB) / DIV + 50;
		return scale(BigDecimal.valueOf(cr));
	}

	public BigDecimal critDamage() {
		final double cd = 200 * (crit - SUB) / DIV + 1400;
		return scale(BigDecimal.valueOf(cd));
	}

	public BigDecimal directHitChance() {
		final double dh = 550 * (directHit - SUB) / DIV;
		return scale(BigDecimal.valueOf(dh));
	}

	public BigDecimal averageCritMultiplier() {
		return averageExpectedMultiplier(critChance(), critDamage());
	}

	public BigDecimal averageDirectHitMultiplier() {
		return averageExpectedMultiplier(directHitChance(), DH_MULTIPLIER);
	}

	private BigDecimal averageExpectedMultiplier(BigDecimal rate, BigDecimal multiplier) {
		return rate.multiply(multiplier.subtract(BigDecimal.ONE)).add(BigDecimal.ONE);
	}

	public BigDecimal determinationMultiplier() {
		final double det = 140 * (determination - MAIN) / DIV + 1000;
		return scale(BigDecimal.valueOf(det));
	}

	public BigDecimal autoAttackMultiplier() {
		final double aa = 130 * (speed - SUB) / DIV + 1000;
		return scale(BigDecimal.valueOf(aa));
	}

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

	public BigDecimal tenacityMultiplier() {
		final double ten = 100 * (tenacity - SUB) / DIV + 1000;
		return scale(BigDecimal.valueOf(ten));
	}

	private double gcdModifier() {
		final double gcdMod = Math.ceil(130 * (SUB - speed) / DIV) + 1000;
		return gcdMod / 1000;
	}

	static class Builder {
		int attackPower = 0;
		int weaponDamage = 0;
		int delay = 0;
		int attackSpeed = 0;
		int tenacity = 0;
		int directHit = 0;
		int crit = 0;
		int determination = 0;
		int speed = 0;

		public Builder withAttackPower(int attackPower) {
			this.attackPower = attackPower;
			return this;
		}

		public Builder withWeaponDamage(int weaponDamage) {
			this.weaponDamage = weaponDamage;
			return this;
		}

		public Builder withDelay(int delay) {
			this.delay = delay;
			return this;
		}

		public Builder withAttackSpeed(int attackSpeed) {
			this.attackSpeed = attackSpeed;
			return this;
		}

		public Builder withTenacity(int tenacity) {
			this.tenacity = tenacity;
			return this;
		}

		public Builder withDirectHit(int directHit) {
			this.directHit = directHit;
			return this;
		}

		public Builder withCrit(int crit) {
			this.crit = crit;
			return this;
		}

		public Builder withDetermination(int determination) {
			this.determination = determination;
			return this;
		}

		public Builder withSpeed(int speed) {
			this.speed = speed;
			return this;
		}

		public Stats build() {
			return new Stats(
				attackPower,
				weaponDamage,
				delay,
				attackSpeed,
				tenacity,
				directHit,
				crit,
				determination,
				speed
			);
		}
	}
}
