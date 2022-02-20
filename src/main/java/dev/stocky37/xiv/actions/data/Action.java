package dev.stocky37.xiv.actions.data;

import static dev.stocky37.xiv.actions.util.Util.slugify;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.List;

public interface Action extends ApiObject {

	boolean onGCD();

	@JsonFormat(pattern = "MILLIS")
	Duration cast();

	@JsonFormat(pattern = "MILLIS")
	Duration recast();

	List<StatusEffect> effects();

	@JsonProperty
	Type actionType();

	enum Type {
		ABILITY, ITEM;
		@Override
		public String toString() {
			return slugify(name());
		}
	}
}
