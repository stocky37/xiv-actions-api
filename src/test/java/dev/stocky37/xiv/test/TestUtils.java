package dev.stocky37.xiv.test;

import dev.stocky37.xiv.model.Attribute;
import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.Stats;

public class TestUtils {

	public static Job reaper() {
		return Job.builder()
			.withId("39")
			.withName("reaper")
			.withAbbreviation("RPR")
			.withType(Job.Type.JOB)
			.withPrimaryStat(Attribute.STRENGTH)
			.withCategory(Job.Category.DOW)
			.withRole(Job.Role.MELEE_DPS)
			.withLimited(false)
			.build();
	}

	public static DerivedStats defaultStats() {
		return new DerivedStats(defaultBaseStats(), Attribute.STRENGTH);
	}

	public static Stats defaultBaseStats() {
		return Stats.builder()
			.strength(2556)
			.crit(2217)
			.determination(1576)
			.directHit(1377)
			.skillSpeed(436)
			.physicalDamage(120)
			.autoAttack(128)
			.delay(3.2)
			.build();
	}

	public static DerivedStats statsWithCrit(int crit) {
		return new DerivedStats(Stats.builder().crit(crit).build(), Attribute.STRENGTH);
	}

	public static DerivedStats statsWithDirectHit(int dh) {
		return new DerivedStats(Stats.builder().directHit(dh).build(), Attribute.STRENGTH);
	}

	public static DerivedStats statsWithDet(int det) {
		return new DerivedStats(Stats.builder().determination(det).build(), Attribute.STRENGTH);
	}

	public static DerivedStats statsWithSkillSpeed(int speed) {
		return new DerivedStats(Stats.builder().skillSpeed(speed).build(), Attribute.STRENGTH);
	}

	public static DerivedStats statsWithSpellSpeed(int speed) {
		return new DerivedStats(Stats.builder().spellSpeed(speed).build(), Attribute.INTELLIGENCE);
	}

	public static DerivedStats statsWithTenacity(int ten) {
		return new DerivedStats(Stats.builder().tenacity(ten).build(), Attribute.STRENGTH);
	}

	public static DerivedStats statsWithStrength(int str) {
		return new DerivedStats(Stats.builder().strength(str).build(), Attribute.STRENGTH);
	}

	public static DerivedStats statsWithDexterity(int dex) {
		return new DerivedStats(Stats.builder().dexterity(dex).build(), Attribute.DEXTERITY);
	}

	public static DerivedStats statsWithIntelligence(int intel) {
		return new DerivedStats(Stats.builder().intelligence(intel).build(), Attribute.INTELLIGENCE);
	}

	public static DerivedStats statsWithMind(int mind) {
		return new DerivedStats(Stats.builder().mind(mind).build(), Attribute.MIND);
	}
}
