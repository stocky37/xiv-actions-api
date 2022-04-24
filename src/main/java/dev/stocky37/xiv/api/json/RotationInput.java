package dev.stocky37.xiv.api.json;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public record RotationInput(String job, List<Action> rotation) {
	public record Action(
		dev.stocky37.xiv.model.Action.Type type,
		Optional<Duration> length,
		Optional<String> id
	) {}
}
