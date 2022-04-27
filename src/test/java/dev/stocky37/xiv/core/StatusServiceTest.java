package dev.stocky37.xiv.core;

import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class StatusServiceTest {

	@Inject StatusService statuses;

	@Test
	void findById() {
		System.out.println(statuses.findById("2586"));
	}
}
