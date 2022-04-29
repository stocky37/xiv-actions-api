package dev.stocky37.xiv.model;

import dev.stocky37.xiv.core.Timeline;
import java.time.Duration;
import java.util.Collection;

public record OGcdEvent(
	Duration timestamp,
	Action action,
	long damage,
	Collection<ActiveStatus> statusEffects
) implements Timeline.Event {
	@Override
	public Type type() {
		return Type.ACTION;
	}

	@Override
	public boolean isGCD() {
		return false;
	}
}
