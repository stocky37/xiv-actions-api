package dev.stocky37.xiv.model;

import com.fasterxml.jackson.annotation.JsonView;
import dev.stocky37.xiv.api.json.Views;
import dev.stocky37.xiv.util.Util;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

public record Ability(
	// ApiObject
	String id,
	String name,
	URI icon,

	// Action
	Boolean onGCD,
	Duration cast,
	Duration recast,
	Set<String> cooldownGroups,
	Integer potency,
	List<Status> statusEffects,

	// Ability
	@JsonView(Views.Standard.class) URI hdIcon,
	@JsonView(Views.Standard.class) Integer level,
	@JsonView(Views.Standard.class) AbilityType abilityType,
	@JsonView(Views.Standard.class) Boolean isRoleAction,
	@JsonView(Views.Standard.class) Optional<DamageType> damageType,
	@JsonView(Views.Standard.class) Optional<String> comboFrom
) implements Action, ApiObject {

	public enum DamageType {
		PHYSICAL, MAGICAL;

		@Override
		public String toString() {
			return Util.slugify(name());
		}
	}

	public enum AbilityType {
		SPELL, ABILITY, WEAPONSKILL;

		@Override
		public String toString() {
			return Util.slugify(name());
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(Ability action) {
		return new Builder(action);
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
		private boolean onGCD;
		private Duration cast;
		private Duration recast;
		private int level;
		private AbilityType abilityType;
		private boolean isRoleAction;
		private Optional<DamageType> damageType = Optional.empty();
		private Optional<String> comboFrom = Optional.empty();
		private Set<String> cooldownGroups = new HashSet<>();
		private int potency = 0;
		private List<Status> statusEffects = new ArrayList<>();

		private Builder() {}

		public Builder(Ability action) {
			this.id = action.id;
			this.name = action.name;
			this.icon = action.icon;
			this.hdIcon = action.hdIcon;
			this.onGCD = action.onGCD;
			this.cast = action.cast;
			this.recast = action.recast;
			this.level = action.level;
			this.abilityType = action.abilityType;
			this.isRoleAction = action.isRoleAction;
			this.damageType = action.damageType;
			this.comboFrom = action.comboFrom;
			this.cooldownGroups = action.cooldownGroups;
			this.potency = action.potency;
			this.statusEffects = action.statusEffects;
		}

		public Ability build() {
			return new Ability(
				id,
				name,
				icon,
				onGCD,
				cast,
				recast,
				cooldownGroups,
				potency,
				statusEffects,
				hdIcon,
				level,
				abilityType,
				isRoleAction,
				damageType,
				comboFrom
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

		public Builder withIcon(URI icon) {
			this.icon = icon;
			return this;
		}

		public Builder withIconHD(URI iconHD) {
			this.hdIcon = iconHD;
			return this;
		}

		public Builder withComboFrom(@Nullable String comboFrom) {
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

		public Builder withPotency(int potency) {
			this.potency = potency;
			return this;
		}

		public Builder withStatusEffects(List<Status> statusEffects) {
			this.statusEffects = statusEffects;
			return this;
		}
	}
}

