package dev.stocky37.xiv.model;

import static dev.stocky37.xiv.util.Util.JOB_MODIFIER;
import static dev.stocky37.xiv.util.Util.floor;
import static dev.stocky37.xiv.util.Util.floorLong;
import static dev.stocky37.xiv.util.Util.scale;

import java.time.Duration;

public record DerivedStats(Stats stats, Attribute primaryStat, LevelMod mod) {

	// level 90 level modifiers
	// source: https://www.akhmorning.com/allagan-studies/modifiers/levelmods/
	public static final LevelMod LVL90 = new LevelMod(390, 400, 1900, 195);
	public static final double DH_MULTIPLIER = 1.25;


	public DerivedStats(Stats stats, Attribute primaryStat) {
		this(stats, primaryStat, LVL90);
	}

	public double critChance() {
		return scale(200 * (stats.crit() - mod.sub()) / (double) mod.div() + 50);
	}

	public double critDamage() {
		return scale(200 * (stats.crit() - mod.sub()) / (double) mod.div() + 1400);
	}

	public double directHitChance() {
		return scale(550 * (stats.directHit() - mod.sub()) / (double) mod.div());
	}

	public double directHitDamage() {
		return DH_MULTIPLIER;
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

	public int weaponDamage() {
		return switch(primaryStat) {
			case STRENGTH, DEXTERITY -> stats.physicalDamage();
			case MIND, INTELLIGENCE -> stats.magicalDamage();
			default -> throw new IllegalStateException("Unhandled primary stat: " + primaryStat());
		};
	}

	public Duration gcd(Duration baseGcd) {
		return Duration.ofMillis(floorLong(baseGcd.toMillis() * (gcdModifier()) / 10) * 10);
	}

	public int fspeed() {
		return 130 * (attackSpeed() - mod.sub()) / mod.div() + 1000;
	}

	public int fatk() {
		return floor(mod.fatk() * (attackPower() - mod.main()) / (double) mod.main() + 100);
	}

	public int fwd() {
		return floor(mod.main() * JOB_MODIFIER / 1000d + weaponDamage());
	}

	public double fdet() {
		return scale(140 * (stats.determination() - mod.main()) / (double) mod.div() + 1000);
	}

	private double gcdModifier() {
		return 2 - scale(fspeed());
	}

	public record LevelMod(int main, int sub, int div, int fatk) {}

}
