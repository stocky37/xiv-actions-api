package dev.stocky37.xiv.model;

import java.time.Duration;

public record StatusEffect(
	String id,
	String name,
	Duration length
) {

}
