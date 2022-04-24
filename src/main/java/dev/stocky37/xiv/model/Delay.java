package dev.stocky37.xiv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Duration;
import java.util.Optional;

public record Delay(@JsonIgnore Duration duration) implements Action {
	@Override
	public Boolean onGCD() {
		return false;
	}

	@Override
	public Integer potency() {
		return 0;
	}

	@Override
	public Type actionType() {
		return Type.DELAY;
	}

	@Override
	public Optional<Duration> animationLock() {
		return Optional.of(duration);
	}
}
