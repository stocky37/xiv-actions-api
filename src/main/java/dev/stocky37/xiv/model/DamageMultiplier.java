package dev.stocky37.xiv.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("damage")
public record DamageMultiplier(double multiplier) implements Status.Effect<Double> {
	@Override
	public Status.Type type() {
		return Status.Type.DAMAGE;
	}

	@Override
	public Double modify(Double damage) {
		return damage * multiplier;
	}
}
