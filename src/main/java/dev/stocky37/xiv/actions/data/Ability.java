package dev.stocky37.xiv.actions.data;

import static dev.stocky37.xiv.actions.util.Util.slugify;

import com.fasterxml.jackson.annotation.JsonView;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public record Ability(
	// ApiObject
	String id,
	String name,
	URI icon,
	URI hdIcon,
	String description,

	// Action
	boolean onGCD,
	Duration cast,
	Duration recast,

	// Ability
	@JsonView(Views.Standard.class) int level,
	@JsonView(Views.Standard.class) AbilityType abilityType,
	@JsonView(Views.Standard.class) boolean isRoleAction,
	@JsonView(Views.Standard.class) Optional<DamageType> damageType,
	@JsonView(Views.Standard.class) Optional<String> comboFrom,
	@JsonView(Views.Standard.class) Set<String> cooldownGroups
) implements Action, ApiObject {

	public enum DamageType {
		PHYSICAL, MAGICAL;

		@Override
		public String toString() {
			return slugify(name());
		}
	}

	public enum AbilityType {
		SPELL, ABILITY, WEAPONSKILL;

		@Override
		public String toString() {
			return slugify(name());
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(Ability action) {
		return new Builder(action);
	}

	@Override
	public List<StatusEffect> effects() {
		return new ArrayList<>();
	}

	@Override
	public Type actionType() {
		return Type.ABILITY;
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class Builder {
		private String id;
		private String name;
		private URI icon;
		private URI hdIcon;
		private String description;
		private boolean onGCD;
		private Duration cast;
		private Duration recast;
		private int level;
		private AbilityType abilityType;
		private boolean isRoleAction;
		private Optional<DamageType> damageType = Optional.empty();
		private Optional<String> comboFrom = Optional.empty();
		private Set<String> cooldownGroups = new HashSet<>();

		private Builder() {}

		public Builder(Ability action) {
			this.id = action.id;
			this.name = action.name;
			this.icon = action.icon;
			this.hdIcon = action.hdIcon;
			this.description = action.description;
			this.onGCD = action.onGCD;
			this.cast = action.cast;
			this.recast = action.recast;
			this.level = action.level;
			this.abilityType = action.abilityType;
			this.isRoleAction = action.isRoleAction;
			this.damageType = action.damageType;
			this.comboFrom = action.comboFrom;
			this.cooldownGroups = action.cooldownGroups;
		}

		public Ability build() {
			return new Ability(
				id,
				name,
				icon,
				hdIcon,
				description,
				onGCD,
				cast,
				recast,
				level,
				abilityType,
				isRoleAction,
				damageType,
				comboFrom,
				cooldownGroups
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

		public Builder withAbilityType(AbilityType abilityType) {
			this.abilityType = abilityType;
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
			this.hdIcon = iconHD;
			return this;
		}

		public Builder withComboFrom(String comboFrom) {
			this.comboFrom = Optional.ofNullable(comboFrom);
			return this;
		}

		public Builder withCooldownGroups(Set<String> cooldownGroups) {
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

