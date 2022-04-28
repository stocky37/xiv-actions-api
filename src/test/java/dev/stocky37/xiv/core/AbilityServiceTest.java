package dev.stocky37.xiv.core;

import dev.stocky37.xiv.model.Ability;
import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.test.TestUtils;
import io.quarkus.test.junit.QuarkusTest;
import java.net.URI;
import java.time.Duration;
import javax.inject.Inject;
import javax.inject.Named;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class AbilityServiceTest implements WithAssertions {

	@Inject
	AbilityService abilities;

	@Inject
	@Named("abilities.shadow-of-death")
	Ability shadowOfDeath;

	@Test
	void findForJob() {
		assertThat(abilities.findForJob("rpr")).hasSize(39);
	}

	@Test
	void findById() {
		assertThat(abilities.findById("24378").orElseThrow()).isEqualTo(shadowOfDeath);
	}
}

