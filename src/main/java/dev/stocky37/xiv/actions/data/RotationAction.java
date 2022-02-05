package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Duration;
import java.util.Optional;

public record RotationAction(
	Optional<Action> action,
	Optional<Item> item,
	@JsonFormat(pattern = "MILLIS") Duration timestamp,
	Optional<Integer> gcdNumber
) {

}
