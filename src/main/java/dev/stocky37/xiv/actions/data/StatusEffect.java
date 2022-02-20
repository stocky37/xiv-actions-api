package dev.stocky37.xiv.actions.data;

import java.time.Duration;

public record StatusEffect(
	String id,
	String name,
	Duration length
) {

}
