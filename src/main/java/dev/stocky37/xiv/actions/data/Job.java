package dev.stocky37.xiv.actions.data;

import static dev.stocky37.xiv.actions.util.Util.slugify;

import com.fasterxml.jackson.annotation.JsonView;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Job(
	String id,
	String name,
	String abbreviation,
	URI icon,
	Category category,
	Type type,
	Role role,
	int index,
	boolean isLimited,
	@JsonView(Views.Detailed.class) Optional<Attribute> primaryStat,
	@JsonView(Views.Detailed.class) List<Ability> abilities,
	@JsonView(Views.Detailed.class) List<Consumable> potions
) {

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(Job action) {
		return new Builder(action);
	}

	public enum Category {
		DOW,
		DOM,
		DOH,
		DOL;

		@Override
		public String toString() {
			return slugify(this.name());
		}
	}

	public enum Type {
		CLASS,
		JOB;

		@Override
		public String toString() {
			return slugify(this.name());
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
			return slugify(this.name());
		}
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class Builder {
		private String id;
		private String name;
		private String abbreviation;
		private URI icon;
		private Category category;
		private Type type;
		private Role role;
		private int index;
		private boolean isLimited;
		private Optional<Attribute> primaryStat = Optional.empty();
		private List<Ability> abilities = new ArrayList<>();
		private List<Consumable> potions = new ArrayList<>();

		private Builder() {}

		public Builder(Job job) {
			this.id = job.id;
			this.name = job.name;
			this.abbreviation = job.abbreviation;
			this.icon = job.icon;
			this.category = job.category;
			this.type = job.type;
			this.role = job.role;
			this.index = job.index;
			this.isLimited = job.isLimited;
			this.primaryStat = job.primaryStat;
			this.abilities = job.abilities;
			this.potions = job.potions;
		}

		public Job build() {
			return new Job(
				id,
				name,
				abbreviation,
				icon,
				category,
				type,
				role,
				index,
				isLimited,
				primaryStat,
				abilities,
				potions
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

		public Builder withAbbreviation(String abbreviation) {
			this.abbreviation = abbreviation;
			return this;
		}

		public Builder withIcon(URI icon) {
			this.icon = icon;
			return this;
		}

		public Builder withCategory(Category category) {
			this.category = category;
			return this;
		}

		public Builder withType(Type type) {
			this.type = type;
			return this;
		}

		public Builder withRole(Role role) {
			this.role = role;
			return this;
		}

		public Builder withIndex(int index) {
			this.index = index;
			return this;
		}

		public Builder withLimited(boolean limited) {
			isLimited = limited;
			return this;
		}

		public Builder withPrimaryStat(Attribute primaryStat) {
			this.primaryStat = Optional.ofNullable(primaryStat);
			return this;
		}

		public Builder withActions(List<Ability> abilities) {
			this.abilities = abilities;
			return this;
		}

		public Builder withPotions(List<Consumable> potions) {
			this.potions = potions;
			return this;
		}
	}
}
