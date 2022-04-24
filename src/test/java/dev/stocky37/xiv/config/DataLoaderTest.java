package dev.stocky37.xiv.config;

import dev.stocky37.xiv.model.Ability;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class DataLoaderTest implements WithAssertions {
	@Inject
	@Named("data.actions")
	Map<String, Ability> data;

	@Test
	public void loadedActionData() {
		assertThat(data).isNotEmpty();
	}
}
