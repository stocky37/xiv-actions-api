package dev.stocky37.xiv.core;

import dev.stocky37.xiv.model.Status;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.inject.Named;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class StatusServiceTest implements WithAssertions {

	@Inject StatusService statuses;

	@Inject
	@Named("statuses.deaths-design")
	Status deathsDesign;

	@Test
	void findById() {
		assertThat(statuses.findById("2586").orElseThrow()).isEqualTo(deathsDesign);
	}
}
