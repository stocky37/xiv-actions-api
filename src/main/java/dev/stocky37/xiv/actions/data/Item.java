package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import java.net.URI;
import java.time.Duration;
import java.util.List;

public record Item(
	//  ApiObject
	String id,
	String name,
	URI icon,
	URI hdIcon,
	String description,

	// Action
	Duration recast,

	// Ability
	@JsonView(Views.Standard.class) @JsonFormat(pattern = "MILLIS") Duration bonusDuration,
	@JsonView(Views.Standard.class) List<Bonus> bonuses
) implements Action {

	public record Bonus(Attribute attribute, int value, int max) {
	}

	@JsonProperty
	public boolean onGCD() {
		return false;
	}

	@Override
	public Duration cast() {
		return Duration.ZERO;
	}

	@Override
	public Type actionType() {
		return Type.ITEM;
	}

	// temporarily adding medicated status effect manually
	@Override
	public List<StatusEffect> effects() {
		return Lists.newArrayList(new StatusEffect("49", "Medicated", bonusDuration));
	}
}

