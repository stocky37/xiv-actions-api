package dev.stocky37.xiv.model;

import dev.stocky37.xiv.core.Timeline;
import java.time.Duration;

public record AutoAttackEvent(Duration timestamp, long damage) implements Timeline.Event {
	@Override
	public Type type() {
		return Type.AUTO_ATTACK;
	}

	@Override
	public boolean isGCD() {
		return false;
	}
}
