package dev.stocky37.xiv.actions.data;

import java.net.URI;
import java.time.Duration;
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
	Duration recast,
	Duration cast,
	boolean isRoleAction,
	int level,
	boolean onGCD
) {
}
