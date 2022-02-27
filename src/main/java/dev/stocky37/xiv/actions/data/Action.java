package dev.stocky37.xiv.actions.data;

import static dev.stocky37.xiv.actions.util.Util.slugify;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface Action {
	@JsonProperty
	@JsonView(Views.Rotation.class)
	Type actionType();

	@JsonView(Views.Standard.class)
	boolean onGCD();

	@JsonFormat(pattern = "MILLIS")
	@JsonView(Views.Standard.class)
	default Duration cast() {
		return Duration.ZERO;
	}

	@JsonFormat(pattern = "MILLIS")
	@JsonView(Views.Standard.class)
	default Duration recast() {
		return Duration.ZERO;
	}

	@JsonIgnore
	@JsonView(Views.Standard.class)
	default List<StatusEffect> effects() {
		return Collections.emptyList();
	}

	@JsonProperty
	@JsonView(Views.Rotation.class)
	default Optional<Duration> animationLock() {
		return Optional.empty();
	}

	enum Type {
		ABILITY, ITEM, DELAY;

		@Override
		public String toString() {
			return slugify(name());
		}
	}
}
