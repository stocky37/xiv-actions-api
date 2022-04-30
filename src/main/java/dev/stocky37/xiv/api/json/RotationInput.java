package dev.stocky37.xiv.api.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.model.Stats;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public record RotationInput(String job, Stats stats, List<ActionRef> rotation) {
	public record ActionRef(
		Action.Type type,
		@JsonFormat(pattern = "MILLIS") Optional<Duration> length,
		Optional<String> id
	) {}
}
