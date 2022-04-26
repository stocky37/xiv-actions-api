package dev.stocky37.xiv.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public record Stats(
	// primary stats
	int strength,
	int dexterity,
	int vitality,
	int intelligence,
	int mind,

	// secondary stats
	int crit,
	int determination,
	int directHit,
	int skillSpeed,
	int spellSpeed,

	// role specific stats
	int tenacity,
	int piety,

	// weapon stats
	int physicalDamage,
	int magicalDamage,
	int delay
) {

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		int strength;
		int dexterity;
		int vitality;
		int intelligence;
		int mind;
		int crit;
		int determination;
		int directHit;
		int skillSpeed;
		int spellSpeed;
		int tenacity;
		int piety;
		int physicalDamage;
		int magicalDamage;
		int delay;

		public Builder strength(int strength) {
			this.strength = strength;
			return this;
		}

		public Builder dexterity(int dexterity) {
			this.dexterity = dexterity;
			return this;
		}

		public Builder vitality(int vitality) {
			this.vitality = vitality;
			return this;
		}

		public Builder intelligence(int intelligence) {
			this.intelligence = intelligence;
			return this;
		}

		public Builder mind(int mind) {
			this.mind = mind;
			return this;
		}

		public Builder crit(int crit) {
			this.crit = crit;
			return this;
		}

		public Builder determination(int determination) {
			this.determination = determination;
			return this;
		}

		public Builder directHit(int directHit) {
			this.directHit = directHit;
			return this;
		}

		public Builder skillSpeed(int skillSpeed) {
			this.skillSpeed = skillSpeed;
			return this;
		}

		public Builder spellSpeed(int spellSpeed) {
			this.spellSpeed = spellSpeed;
			return this;
		}

		public Builder tenacity(int tenacity) {
			this.tenacity = tenacity;
			return this;
		}

		public Builder piety(int piety) {
			this.piety = piety;
			return this;
		}

		public Builder physicalDamage(int physicalDamage) {
			this.physicalDamage = physicalDamage;
			return this;
		}

		public Builder magicalDamage(int magicalDamage) {
			this.magicalDamage = magicalDamage;
			return this;
		}

		public Builder delay(int delay) {
			this.delay = delay;
			return this;
		}

		public Stats build() {
			return new Stats(
				strength,
				dexterity,
				vitality,
				intelligence,
				mind,
				crit,
				determination,
				directHit,
				skillSpeed,
				spellSpeed,
				tenacity,
				piety,
				physicalDamage,
				magicalDamage,
				delay
			);
		}
	}


}
