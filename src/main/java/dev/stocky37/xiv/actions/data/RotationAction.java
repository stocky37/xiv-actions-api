package dev.stocky37.xiv.actions.data;

import java.time.Duration;
import java.util.Optional;

public record RotationAction(
	Optional<Action> action,
	Optional<Item> item,
	Duration timestamp,
	Optional<Integer> gcdNumber
) {
	
}
