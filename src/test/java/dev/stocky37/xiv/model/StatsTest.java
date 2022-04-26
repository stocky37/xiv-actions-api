package dev.stocky37.xiv.model;

import java.time.Duration;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class StatsTest implements WithAssertions {

	private static final Duration BASE_GCD = Duration.ofMillis(2500);
	private static final Duration SHORT_GCD = Duration.ofMillis(1500);
	private static final Duration LONG_GCD = Duration.ofMillis(4000);

	private Stats withCrit(int crit) {
		return Stats.builder().withCrit(crit).build();
	}

	private Stats withDirectHit(int dh) {
		return Stats.builder().withDirectHit(dh).build();
	}

	private Stats withDet(int det) {
		return Stats.builder().withDetermination(det).build();
	}

	private Stats withSpeed(int speed) {
		return Stats.builder().withSpeed(speed).build();
	}

	private Stats withTenacity(int ten) {
		return Stats.builder().withTenacity(ten).build();
	}

	@Test
	void critChance() {
		assertThat(withCrit(2006).critChance()).isEqualTo("0.219");
		assertThat(withCrit(2014).critChance()).isEqualTo("0.219");
		assertThat(withCrit(2015).critChance()).isEqualTo("0.220");
	}

	@Test
	void critDamage() {
		assertThat(withCrit(2006).critDamage()).isEqualTo("1.569");
		assertThat(withCrit(2014).critDamage()).isEqualTo("1.569");
		assertThat(withCrit(2015).critDamage()).isEqualTo("1.570");
	}

	@Test
	void critMultiplier() {
		assertThat(withCrit(2006).averageCritMultiplier()).isEqualTo("1.124611");
		assertThat(withCrit(2014).averageCritMultiplier()).isEqualTo("1.124611");
		assertThat(withCrit(2015).averageCritMultiplier()).isEqualTo("1.125400");
	}

	@Test
	void directHitChance() {
		assertThat(withDirectHit(2000).directHitChance()).isEqualTo("0.463");
		assertThat(withDirectHit(2002).directHitChance()).isEqualTo("0.463");
		assertThat(withDirectHit(2003).directHitChance()).isEqualTo("0.464");
	}

	@Test
	void directHitMultiplier() {
		assertThat(withDirectHit(2000).averageDirectHitMultiplier()).isEqualTo("1.11575");
		assertThat(withDirectHit(2002).averageDirectHitMultiplier()).isEqualTo("1.11575");
		assertThat(withDirectHit(2003).averageDirectHitMultiplier()).isEqualTo("1.11600");
	}

	@Test
	void determinationMultiplier() {
		assertThat(withDet(1996).determinationMultiplier()).isEqualTo("1.118");
		assertThat(withDet(2004).determinationMultiplier()).isEqualTo("1.118");
		assertThat(withDet(2005).determinationMultiplier()).isEqualTo("1.119");
	}

	@Test
	void autoAttackMultiplier() {
		assertThat(withSpeed(1000).autoAttackMultiplier()).isEqualTo("1.041");
		assertThat(withSpeed(1013).autoAttackMultiplier()).isEqualTo("1.041");
		assertThat(withSpeed(1014).autoAttackMultiplier()).isEqualTo("1.042");
	}

	@Test
	void baseGcd() {
		assertThat(withSpeed(1000).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2390));
		assertThat(withSpeed(1057).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2390));
		assertThat(withSpeed(1058).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2380));
		assertThat(withSpeed(1116).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2380));
		assertThat(withSpeed(1117).gcd(BASE_GCD)).isEqualTo(Duration.ofMillis(2370));
	}

	@Test
	void shortGcd() {
		assertThat(withSpeed(1000).gcd(SHORT_GCD)).isEqualTo(Duration.ofMillis(1430));
		assertThat(withSpeed(1086).gcd(SHORT_GCD)).isEqualTo(Duration.ofMillis(1430));
		assertThat(withSpeed(1087).gcd(SHORT_GCD)).isEqualTo(Duration.ofMillis(1420));
	}

	@Test
	void longGcd() {
		assertThat(withSpeed(1000).gcd(LONG_GCD)).isEqualTo(Duration.ofMillis(3830));
		assertThat(withSpeed(1028).gcd(LONG_GCD)).isEqualTo(Duration.ofMillis(3830));
		assertThat(withSpeed(1029).gcd(LONG_GCD)).isEqualTo(Duration.ofMillis(3820));
	}

	@Test
	void tenacity() {
		assertThat(withTenacity(2015).tenacityMultiplier()).isEqualTo("1.085");
		assertThat(withTenacity(2033).tenacityMultiplier()).isEqualTo("1.085");
		assertThat(withTenacity(2034).tenacityMultiplier()).isEqualTo("1.086");
	}
}
