package dev.stocky37.xiv.core;

import static dev.stocky37.xiv.test.TestUtils.defaultStats;
import static dev.stocky37.xiv.test.TestUtils.reaper;
import static dev.stocky37.xiv.test.TestUtils.statsWithCrit;
import static dev.stocky37.xiv.test.TestUtils.statsWithDirectHit;

import dev.stocky37.xiv.model.DerivedStats;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

public class DamageCalculatorTest implements WithAssertions {
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

	@Test
	void expectedAutoDamage() {
		assertThat((int) (CALC.expectedAutoDamage() * 1.1)).isEqualTo(2682);
	}

	private DamageCalculator withStats(DerivedStats stats) {
		return new DamageCalculator(reaper(), stats);
	}

}
