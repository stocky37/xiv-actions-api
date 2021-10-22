package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Optional;
import java.util.Set;

public record Action(
	String id,
	String name,
	String category,
	String description,
	String icon,
	String iconHD,
	Optional<Integer> comboFrom,
	Set<Integer> cooldownGroups,
	int recast,
	int cast,
	boolean isRoleAction,
	int level,
	@JsonIgnore int gcdCooldownGroup
) {

	public boolean isOnGCD() {
		return cooldownGroups.contains(gcdCooldownGroup);
	}
}
