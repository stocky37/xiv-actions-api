package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.net.URI;
import java.time.Duration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public record Action(
	String id,
	String name,
	String category,
	String description,
	URI icon,
	URI iconHD,
	Optional<Integer> comboFrom,
	Set<Integer> cooldownGroups,
	@JsonFormat(pattern = "MILLIS") Duration recast,
	@JsonFormat(pattern = "MILLIS") Duration cast,
	boolean isRoleAction,
	int level,
	boolean onGCD,
	Optional<DamageType> damageType
) {

	public enum DamageType {
		PHYSICAL, MAGICAL;

		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(Action action) {
		return new Builder(action);
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class Builder {
		private String id;
		private String name;
		private String category;
		private String description;
		private URI icon;
		private URI iconHD;
		private Optional<Integer> comboFrom = Optional.empty();
		private Set<Integer> cooldownGroups = new HashSet<>();
		private Duration recast;
		private Duration cast;
		private boolean isRoleAction;
		private int level;
		private boolean onGCD;
		private Optional<DamageType> damageType = Optional.empty();

		private Builder() {}

		public Builder(Action action) {
			this.id = action.id;
			this.name = action.name;
			this.category = action.category;
			this.description = action.description;
			this.icon = action.icon;
			this.iconHD = action.iconHD;
			this.comboFrom = action.comboFrom;
			this.cooldownGroups = action.cooldownGroups;
			this.recast = action.recast;
			this.cast = action.cast;
			this.isRoleAction = action.isRoleAction;
			this.level = action.level;
			this.onGCD = action.onGCD;
			this.damageType = action.damageType;
		}

		public Action build() {
			return new Action(
				id,
				name,
				category,
				description,
				icon,
				iconHD,
				comboFrom,
				cooldownGroups,
				recast,
				cast,
				isRoleAction,
				level,
				onGCD,
				damageType
			);
		}

		public Builder withId(String id) {
			this.id = id;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withCategory(String category) {
			this.category = category;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withIcon(URI icon) {
			this.icon = icon;
			return this;
		}

		public Builder withIconHD(URI iconHD) {
			this.iconHD = iconHD;
			return this;
		}

		public Builder withComboFrom(Integer comboFrom) {
			this.comboFrom = Optional.ofNullable(comboFrom);
			return this;
		}

		public Builder withCooldownGroups(Set<Integer> cooldownGroups) {
			this.cooldownGroups = cooldownGroups;
			return this;
		}

		public Builder withRecast(Duration recast) {
			this.recast = recast;
			return this;
		}

		public Builder withCast(Duration cast) {
			this.cast = cast;
			return this;
		}

		public Builder withRoleAction(boolean roleAction) {
			isRoleAction = roleAction;
			return this;
		}

		public Builder withLevel(int level) {
			this.level = level;
			return this;
		}

		public Builder withOnGCD(boolean onGCD) {
			this.onGCD = onGCD;
			return this;
		}

		public Builder withDamageType(DamageType damageType) {
			this.damageType = Optional.ofNullable(damageType);
			return this;
		}
	}

}

