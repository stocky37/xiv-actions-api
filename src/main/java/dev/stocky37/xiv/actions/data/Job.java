package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record Job(
	String id,
	String name,
	String abbreviation,
	String icon,
	Category category,
	Type type,
	Role role,
	int index,
	boolean isLimited,
	@JsonView(Views.Full.class) Optional<Attribute> primaryStat,
	@JsonView(Views.Full.class) List<Action> actions,
	@JsonView(Views.Full.class) List<Item> potions
) {

	public Job(Job job, List<Action> actions, List<Item> potions) {
		this(
			job.id,
			job.name,
			job.abbreviation,
			job.icon,
			job.category,
			job.type,
			job.role,
			job.index,
			job.isLimited,
			job.primaryStat,
			actions,
			potions
		);
	}

	public Job(
		String id,
		String name,
		String abbreviation,
		String icon,
		Category category,
		Type type,
		Role role,
		int index,
		boolean isLimited,
		Attribute primaryStat
	) {
		this(
			id,
			name,
			abbreviation,
			icon,
			category,
			type,
			role,
			index,
			isLimited,
			Optional.ofNullable(primaryStat),
			Collections.emptyList(),
			Collections.emptyList()
		);
	}

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
