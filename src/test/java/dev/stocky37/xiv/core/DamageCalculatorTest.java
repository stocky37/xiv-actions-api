package dev.stocky37.xiv.core;

import dev.stocky37.xiv.model.Attribute;
import dev.stocky37.xiv.model.DerivedStats;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.Stats;
import org.assertj.core.api.WithAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

public class DamageCalculatorTest implements WithAssertions {
	private static final Job RPR = Job.builder()
		.withId("39")
		.withName("reaper")
		.withAbbreviation("RPR")
		.withType(Job.Type.JOB)
		.withPrimaryStat(Attribute.STRENGTH)
		.withCategory(Job.Category.DOW)
		.withRole(Job.Role.MELEE_DPS)
		.withLimited(false)
		.build();

	private static final Stats stats = Stats.builder()
		.physicalDamage(115)
		.strength(2282)
		.skillSpeed(485)
		.determination(1901)
		.crit(1835)
		.directHit(1167)
		.build();

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	private static final DamageCalculator CALC =
		new DamageCalculator(RPR, new DerivedStats(stats, RPR.primaryStat().get()));

	@Test
	void fatk() {
		assertThat(CALC.fatk()).isEqualTo(1046);
	}

	@Test
	void fwd() {
		assertThat(CALC.fwd()).isEqualTo(159);
	}

	@Test
	void expectedDamage() {
		assertThat((int) CALC.expectedDamage(300)).isEqualTo(6497);
	}
}
