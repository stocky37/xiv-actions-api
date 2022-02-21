package dev.stocky37.xiv.actions.data;

import static dev.stocky37.xiv.actions.util.Util.slugify;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.Duration;
import java.util.List;

public interface Action extends ApiObject {

	@JsonView(Views.Standard.class)
	boolean onGCD();

	@JsonFormat(pattern = "MILLIS")
	@JsonView(Views.Standard.class)
	Duration cast();

	@JsonFormat(pattern = "MILLIS")
	@JsonView(Views.Standard.class)
	Duration recast();

	@JsonIgnore
	@JsonView(Views.Standard.class)
	List<StatusEffect> effects();

	@JsonProperty
	@JsonView(Views.Rotation.class)
	Type actionType();

	enum Type {
		ABILITY, ITEM;

		@Override
		public String toString() {
			return slugify(name());
		}
	}
}
