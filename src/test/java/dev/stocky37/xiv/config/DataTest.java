package dev.stocky37.xiv.config;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class DataTest implements WithAssertions {
	@Inject
	@Named("data.actions")
	Map<String, JsonNode> data;

	@Test
	public void loadedActionData() {
		assertThat(data).isNotEmpty();
	}
}
