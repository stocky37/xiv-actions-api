package dev.stocky37.xiv.actions.data;

import java.util.Optional;

public record Action(
	int id,
	String name,
	String category,
	String description,
	String icon,
	String iconHD,
	Optional<Integer> comboFrom,
	boolean isGCD,
	int cooldownGroup,
	int recast,
	int cast,
	int castType,
	boolean isRoleAction,
	int level
) {
}
