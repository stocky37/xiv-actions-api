package dev.stocky37.xiv.actions.data;


import java.time.Duration;
import java.util.List;

public record Item(
	String id,
	String name,
	String icon,
	String iconHD,
	String description,
	Duration recast,
	Duration bonusDuration,
	List<Bonus> bonuses
) {
	public record Bonus(Attribute attribute, int value, int max) {
	}
}

