package dev.stocky37.xiv.model;

import static dev.stocky37.xiv.test.TestUtils.defaultStats;
import static dev.stocky37.xiv.test.TestUtils.statsWithCrit;
import static dev.stocky37.xiv.test.TestUtils.statsWithDet;
import static dev.stocky37.xiv.test.TestUtils.statsWithDexterity;
import static dev.stocky37.xiv.test.TestUtils.statsWithDirectHit;
import static dev.stocky37.xiv.test.TestUtils.statsWithIntelligence;
import static dev.stocky37.xiv.test.TestUtils.statsWithMind;
import static dev.stocky37.xiv.test.TestUtils.statsWithSkillSpeed;
import static dev.stocky37.xiv.test.TestUtils.statsWithSpellSpeed;
import static dev.stocky37.xiv.test.TestUtils.statsWithStrength;

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
		assertThat(statsWithStrength(stat).attackPower()).isEqualTo(stat);
		assertThat(statsWithDexterity(stat).attackPower()).isEqualTo(stat);
		assertThat(statsWithIntelligence(stat).attackPower()).isEqualTo(stat);
		assertThat(statsWithMind(stat).attackPower()).isEqualTo(stat);
	}

	@Test
	void attackSpeed() {
		final int stat = 1000;
		assertThat(statsWithSkillSpeed(stat).attackSpeed()).isEqualTo(stat);
		assertThat(statsWithSpellSpeed(stat).attackSpeed()).isEqualTo(stat);
	}

	@Test
	void critChance() {
		assertThat(statsWithCrit(2006).critChance()).isEqualTo(0.219);
		assertThat(statsWithCrit(2014).critChance()).isEqualTo(0.219);
		assertThat(statsWithCrit(2015).critChance()).isEqualTo(0.220);
	}

	@Test
	void critDamage() {
		assertThat(statsWithCrit(2006).critDamage()).isEqualTo(1.569);
		assertThat(statsWithCrit(2014).critDamage()).isEqualTo(1.569);
		assertThat(statsWithCrit(2015).critDamage()).isEqualTo(1.570);
	}

	@Test
	void directHitChance() {
		assertThat(statsWithDirectHit(2000).directHitChance()).isEqualTo(0.463);
		assertThat(statsWithDirectHit(2002).directHitChance()).isEqualTo(0.463);
		assertThat(statsWithDirectHit(2003).directHitChance()).isEqualTo(0.464);
	}

	@Test
	void baseGcdSkillSpeed() {
		assertThat(statsWithSkillSpeed(436).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2490));
		assertThat(statsWithSkillSpeed(1000).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2390));
		assertThat(statsWithSkillSpeed(1057).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2390));
		assertThat(statsWithSkillSpeed(1058).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2380));
	}

	@Test
	void baseGcdSpellSpeed() {
		assertThat(statsWithSpellSpeed(1000).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2390));
		assertThat(statsWithSpellSpeed(1057).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2390));
		assertThat(statsWithSpellSpeed(1058).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2380));
	}

	@Test
	void shortGcd() {
		assertThat(statsWithSkillSpeed(1000).gcd(SHORT_GCD)).isEqualTo(Duration.ofMillis(1430));
		assertThat(statsWithSkillSpeed(1086).gcd(SHORT_GCD)).isEqualTo(Duration.ofMillis(1430));
		assertThat(statsWithSkillSpeed(1087).gcd(SHORT_GCD)).isEqualTo(Duration.ofMillis(1420));
	}

	@Test
	void longGcd() {
		assertThat(statsWithSkillSpeed(1000).gcd(LONG_GCD)).isEqualTo(Duration.ofMillis(3830));
		assertThat(statsWithSkillSpeed(1028).gcd(LONG_GCD)).isEqualTo(Duration.ofMillis(3830));
		assertThat(statsWithSkillSpeed(1029).gcd(LONG_GCD)).isEqualTo(Duration.ofMillis(3820));
	}

	@Test
	void determinationMultiplier() {
		assertThat(statsWithDet(1996).fdet()).isEqualTo(1.118);
		assertThat(statsWithDet(2004).fdet()).isEqualTo(1.118);
		assertThat(statsWithDet(2005).fdet()).isEqualTo(1.119);
	}

	@Test
	void fatk() {
		assertThat(defaultStats().fatk()).isEqualTo(1183);
	}

	@Test
	void fwd() {
		assertThat(defaultStats().fwd()).isEqualTo(164);
	}

	@Test
	void fspeed() {
		assertThat(defaultStats().fspeed()).isEqualTo(1002);
	}


}
