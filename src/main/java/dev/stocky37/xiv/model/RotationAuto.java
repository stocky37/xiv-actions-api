package dev.stocky37.xiv.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Duration;

public record RotationAuto(
	@JsonFormat(pattern = "MILLIS") Duration timestamp,
	long damage
) {
}
