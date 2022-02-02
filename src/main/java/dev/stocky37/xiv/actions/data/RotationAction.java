package dev.stocky37.xiv.actions.data;

import java.time.Duration;
import java.util.Optional;

public record RotationAction(
	Action action,
	Duration timestamp,
	Optional<Integer> gcdNumber
) {

}
