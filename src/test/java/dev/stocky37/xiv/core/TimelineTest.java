package dev.stocky37.xiv.core;

import static dev.stocky37.xiv.test.TestUtils.defaultBaseStats;
import static dev.stocky37.xiv.test.TestUtils.reaper;

import dev.stocky37.xiv.config.XivConfig;
import dev.stocky37.xiv.model.Action;
import io.quarkus.test.junit.QuarkusTest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TimelineTest {

	@Test
	void test() {
		Timeline timeline = new Timeline();
//		timeline.put(Duration.ofSeconds(5), Timeline.Event.autoAttack(123));
//		timeline.put(Duration.ofSeconds(0), Timeline.Event.autoAttack(1234));
//		timeline.put(Duration.ofMillis(2500), Timeline.Event.autoAttack(12345));
//		timeline.put(Duration.ofMillis(3200), Timeline.Event.autoAttack(12346));
		System.out.println(timeline.lastEntry());
		System.out.println(timeline.ceilingKey(Duration.ofSeconds(1)));
		System.out.println(timeline.floorKey(Duration.ofSeconds(1)));
		System.out.println(timeline.firstKey());
		timeline.entrySet().forEach(System.out::println);
	}

	@Inject
	XivConfig config;

	@Inject
	AbilityService abilities;

	@Test
	void testbuilder() {
		final var builder = new RotationBuilder(config, reaper(), defaultBaseStats());

		Map<String, Action> abilityMap = new HashMap<>();

		abilities.findForJob(reaper()).forEach(a -> abilityMap.put(a.id(), a));

		builder.append(abilityMap.get("24378"));
		builder.append(abilityMap.get("24380"));
		builder.append(abilityMap.get("24380"));
		builder.append(abilityMap.get("24385"));
		builder.append(abilityMap.get("24394"));
		builder.append(abilityMap.get("24395"));
		builder.append(abilityMap.get("24396"));

		builder.build();
	}

}
