package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;

public record Job(
	@JsonView(Views.List.class) String id,
	@JsonView(Views.List.class) String name,
	@JsonView(Views.List.class) String abbreviation,
	@JsonView(Views.List.class) String icon,
	@JsonView(Views.List.class) Category category,
	@JsonView(Views.List.class) Type type,
	@JsonView(Views.List.class) Role role,
	@JsonView(Views.List.class) int index,
	@JsonView(Views.List.class) boolean isLimited,
	List<Action> actions
) {

	public enum Category {
		DOW,
		DOM,
		DOH,
		DOL;

		@Override
		public String toString() {
			return this.name().toLowerCase().replace("_", "-");
		}
	}

	public enum Type {
		CLASS,
		JOB;

		@Override
		public String toString() {
			return this.name().toLowerCase().replace("_", "-");
		}
	}

	public enum Role {
		NON_BATTLE,
		TANK,
		MELEE_DPS,
		RANGED_DPS,
		HEALER;

		@Override
		public String toString() {
			return this.name().toLowerCase().replace("_", "-");
		}
	}
}