package dev.stocky37.xiv.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.quarkus.test.junit.QuarkusTest;
import java.io.IOException;
import java.net.URL;
import javax.inject.Inject;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class StatusDeserialisationTest implements WithAssertions {

	@Inject ObjectMapper json;

	@SuppressWarnings("UnstableApiUsage")
	private static final URL file = Resources.getResource("fixtures/statuses/status.yml");

	@Test
	void loadActionData() throws IOException {
		final var status = json.readValue(file, Status.class);
	}


}
