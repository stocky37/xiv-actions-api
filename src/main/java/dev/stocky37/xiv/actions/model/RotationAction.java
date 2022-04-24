package dev.stocky37.xiv.actions.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public record RotationAction(
	Action action,
	@JsonFormat(pattern = "MILLIS") Duration timestamp,
	Optional<Integer> gcdNumber,
	List<RotationEffect> effects
) {
}
