package dev.stocky37.xiv.model;

import dev.stocky37.xiv.core.Timeline;
import java.time.Duration;

public record GcdEvent(Duration timestamp, Action action, long damage, int gcdCount) implements Timeline.Event {
	@Override
	public Type type() {
		return Type.ACTION;
	}

	@Override
	public boolean isGCD() {
		return true;
	}
}
