package dev.stocky37.xiv.config;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.inject.Named;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class DataLoaderTest implements WithAssertions {
	@Inject
	@Named("abilities.data")
	JsonNode abilities;

	@Inject
	@Named("statuses.data")
	JsonNode statuses;

	@Inject
	@Named("jobs.data")
	JsonNode jobs;

	@Test
	public void loadedAbilityData() {
		assertThat(abilities.isMissingNode()).isFalse();
	}

	@Test
	public void loadedStatusData() {
		assertThat(abilities.isMissingNode()).isFalse();
	}

	@Test
	public void loadedJobData() {
		assertThat(abilities.isMissingNode()).isFalse();
	}
}
