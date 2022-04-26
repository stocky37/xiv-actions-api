package dev.stocky37.xiv.core;

import static dev.stocky37.xiv.test.TestUtils.defaultStats;
import static dev.stocky37.xiv.test.TestUtils.reaper;
import static dev.stocky37.xiv.test.TestUtils.statsWithCrit;
import static dev.stocky37.xiv.test.TestUtils.statsWithDet;
import static dev.stocky37.xiv.test.TestUtils.statsWithDirectHit;

import dev.stocky37.xiv.model.DerivedStats;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

public class DamageCalculatorTest implements WithAssertions {
	@SuppressWarnings("OptionalGetWithoutIsPresent")
	private static final DamageCalculator CALC = new DamageCalculator(
		reaper(),
		defaultStats()
	);

	@Test
	void critMultiplier() {
		assertThat(withStats(statsWithCrit(2006)).averageCritMultiplier()).isEqualTo(1.124611);
		assertThat(withStats(statsWithCrit(2014)).averageCritMultiplier()).isEqualTo(1.124611);
		assertThat(withStats(statsWithCrit(2015)).averageCritMultiplier()).isEqualTo(1.125400);
	}

	@Test
	void directHitMultiplier() {
		assertThat(withStats(statsWithDirectHit(2000)).averageDirectHitMultiplier()).isEqualTo(1.11575);
		assertThat(withStats(statsWithDirectHit(2002)).averageDirectHitMultiplier()).isEqualTo(1.11575);
		assertThat(withStats(statsWithDirectHit(2003)).averageDirectHitMultiplier()).isEqualTo(1.11600);
	}


	@Test
	void expectedDamage() {
		assertThat((int) CALC.expectedDamage(300)).isEqualTo(7735);
	}


//	@Test
//	void fauto() {
//		assertThat(CALC.fauto()).isEqualTo(172);
//	}

//	@Test
//	void autoAttackMultiplier() {
//		assertThat(statsWithSpeed(1000).autoAttackMultiplier()).isEqualTo(1.041);
//		assertThat(statsWithSpeed(1013).autoAttackMultiplier()).isEqualTo(1.041);
//		assertThat(statsWithSpeed(1014).autoAttackMultiplier()).isEqualTo(1.042);
//	}

//	@Test
//	void tenacity() {
//		assertThat(statsWithTenacity(2015).tenacityMultiplier()).isEqualTo(1.085);
//		assertThat(statsWithTenacity(2033).tenacityMultiplier()).isEqualTo(1.085);
//		assertThat(statsWithTenacity(2034).tenacityMultiplier()).isEqualTo(1.086);
//	}

	private DamageCalculator withStats(DerivedStats stats) {
		return new DamageCalculator(reaper(), stats);
	}

}
