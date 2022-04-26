package dev.stocky37.xiv.model;

import java.time.Duration;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class DerivedStatsTest implements WithAssertions {

	private static final Duration BASE_GCD = Duration.ofMillis(2500);
	private static final Duration SHORT_GCD = Duration.ofMillis(1500);
	private static final Duration LONG_GCD = Duration.ofMillis(4000);

	@Test
	void attackPower() {
		final int stat = 1000;
		assertThat(withStrength(stat).attackPower()).isEqualTo(stat);
		assertThat(withDexterity(stat).attackPower()).isEqualTo(stat);
		assertThat(withIntelligence(stat).attackPower()).isEqualTo(stat);
		assertThat(withMind(stat).attackPower()).isEqualTo(stat);
	}

	@Test
	void attackSpeed() {
		final int stat = 1000;
		assertThat(withSkillSpeed(stat).attackSpeed()).isEqualTo(stat);
		assertThat(withSpellSpeed(stat).attackSpeed()).isEqualTo(stat);
	}

	@Test
	void critChance() {
		assertThat(withCrit(2006).critChance()).isEqualTo(0.219);
		assertThat(withCrit(2014).critChance()).isEqualTo(0.219);
		assertThat(withCrit(2015).critChance()).isEqualTo(0.220);
	}

	@Test
	void critDamage() {
		assertThat(withCrit(2006).critDamage()).isEqualTo(1.569);
		assertThat(withCrit(2014).critDamage()).isEqualTo(1.569);
		assertThat(withCrit(2015).critDamage()).isEqualTo(1.570);
	}

	@Test
	void directHitChance() {
		assertThat(withDirectHit(2000).directHitChance()).isEqualTo(0.463);
		assertThat(withDirectHit(2002).directHitChance()).isEqualTo(0.463);
		assertThat(withDirectHit(2003).directHitChance()).isEqualTo(0.464);
	}

	@Test
	void baseGcdSkillSpeed() {
		assertThat(withSkillSpeed(1000).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2390));
		assertThat(withSkillSpeed(1057).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2390));
		assertThat(withSkillSpeed(1058).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2380));
	}

	@Test
	void baseGcdSpellSpeed() {
		assertThat(withSpellSpeed(1000).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2390));
		assertThat(withSpellSpeed(1057).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2390));
		assertThat(withSpellSpeed(1058).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2380));
	}

	@Test
	void shortGcd() {
		assertThat(withSkillSpeed(1000).gcd(SHORT_GCD)).isEqualTo(Duration.ofMillis(1430));
		assertThat(withSkillSpeed(1086).gcd(SHORT_GCD)).isEqualTo(Duration.ofMillis(1430));
		assertThat(withSkillSpeed(1087).gcd(SHORT_GCD)).isEqualTo(Duration.ofMillis(1420));
	}

	@Test
	void longGcd() {
		assertThat(withSkillSpeed(1000).gcd(LONG_GCD)).isEqualTo(Duration.ofMillis(3830));
		assertThat(withSkillSpeed(1028).gcd(LONG_GCD)).isEqualTo(Duration.ofMillis(3830));
		assertThat(withSkillSpeed(1029).gcd(LONG_GCD)).isEqualTo(Duration.ofMillis(3820));
	}


	//	@Test
//	void critMultiplier() {
//		assertThat(withCrit(2006).averageCritMultiplier()).isEqualTo(1.124611);
//		assertThat(withCrit(2014).averageCritMultiplier()).isEqualTo(1.124611);
//		assertThat(withCrit(2015).averageCritMultiplier()).isEqualTo(1.125400);
//	}
//
//	@Test
//	void directHitMultiplier() {
//		assertThat(withDirectHit(2000).averageDirectHitMultiplier()).isEqualTo(1.11575);
//		assertThat(withDirectHit(2002).averageDirectHitMultiplier()).isEqualTo(1.11575);
//		assertThat(withDirectHit(2003).averageDirectHitMultiplier()).isEqualTo(1.11600);
//	}
//
//	@Test
//	void determinationMultiplier() {
//		assertThat(withDet(1996).determinationMultiplier()).isEqualTo(1.118);
//		assertThat(withDet(2004).determinationMultiplier()).isEqualTo(1.118);
//		assertThat(withDet(2005).determinationMultiplier()).isEqualTo(1.119);
//	}
//
//	@Test
//	void autoAttackMultiplier() {
//		assertThat(withSpeed(1000).autoAttackMultiplier()).isEqualTo(1.041);
//		assertThat(withSpeed(1013).autoAttackMultiplier()).isEqualTo(1.041);
//		assertThat(withSpeed(1014).autoAttackMultiplier()).isEqualTo(1.042);
//	}
//
//	@Test
//	void tenacity() {
//		assertThat(withTenacity(2015).tenacityMultiplier()).isEqualTo(1.085);
//		assertThat(withTenacity(2033).tenacityMultiplier()).isEqualTo(1.085);
//		assertThat(withTenacity(2034).tenacityMultiplier()).isEqualTo(1.086);
//	}

	private DerivedStats withCrit(int crit) {
		return new DerivedStats(Stats.builder().crit(crit).build(), Attribute.STRENGTH);
	}

	private DerivedStats withDirectHit(int dh) {
		return new DerivedStats(Stats.builder().directHit(dh).build(), Attribute.STRENGTH);
	}

	private DerivedStats withDet(int det) {
		return new DerivedStats(Stats.builder().determination(det).build(), Attribute.STRENGTH);
	}

	private DerivedStats withSkillSpeed(int speed) {
		return new DerivedStats(Stats.builder().skillSpeed(speed).build(), Attribute.STRENGTH);
	}

	private DerivedStats withSpellSpeed(int speed) {
		return new DerivedStats(Stats.builder().spellSpeed(speed).build(), Attribute.INTELLIGENCE);
	}

	private DerivedStats withTenacity(int ten) {
		return new DerivedStats(Stats.builder().tenacity(ten).build(), Attribute.STRENGTH);
	}

	private DerivedStats withStrength(int str) {
		return new DerivedStats(Stats.builder().strength(str).build(), Attribute.STRENGTH);
	}

	private DerivedStats withDexterity(int dex) {
		return new DerivedStats(Stats.builder().dexterity(dex).build(), Attribute.DEXTERITY);
	}

	private DerivedStats withIntelligence(int intel) {
		return new DerivedStats(Stats.builder().intelligence(intel).build(), Attribute.INTELLIGENCE);
	}

	private DerivedStats withMind(int mind) {
		return new DerivedStats(Stats.builder().mind(mind).build(), Attribute.MIND);
	}
}
