package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import dev.stocky37.xiv.actions.util.Util;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class ActionConverter implements Function<JsonNode, Action> {

	public static final String ID = "ID";
	public static final String NAME = "Name";
	public static final String CATEGORY = "ActionCategory.Name";
	public static final String DESCRIPTION = "Description";
	public static final String ICON = "Icon";
	public static final String ICON_HD = "IconHD";
	public static final String COMBO_ACTION = "ActionComboTargetID";
	public static final String COOLDOWN_GROUP = "CooldownGroup";
	public static final String COOLDOWN_GROUP_ALT = "AdditionalCooldownGroup";
	public static final String CAST = "Cast100ms";
	public static final String RECAST = "Recast100ms";
	public static final String ROLE_ACTION = "IsRoleAction";
	public static final String LEVEL = "ClassJobLevel";

	public static final List<String> ALL_FIELDS = List.of(
		ID,
		NAME,
		ICON,
		CATEGORY,
		DESCRIPTION,
		ICON,
		ICON_HD,
		COMBO_ACTION,
		COOLDOWN_GROUP,
		COOLDOWN_GROUP_ALT,
		CAST,
		RECAST,
		ROLE_ACTION,
		LEVEL
	);

	private final int gcdCdGroup;
	private final Util util;

	@Inject
	public ActionConverter(
		@ConfigProperty(name = "gcd-cd-group") int gcdCdGroup,
		Util util
	) {
		this.gcdCdGroup = gcdCdGroup;
		this.util = util;
	}

	@Override
	public Action apply(JsonNode node) {
		final var json = util.wrapNode(node);
		final Set<Integer> cooldownGroups = Sets.newHashSet(
			json.getInt(COOLDOWN_GROUP),
			json.getInt(COOLDOWN_GROUP_ALT)
		);

		return new Action(
			json.getText(ID),
			json.getText(NAME),
			json.getText(CATEGORY).toLowerCase(),
			json.getText(DESCRIPTION),
			json.getUri(ICON),
			json.getUri(ICON_HD),
			json.getInt(COMBO_ACTION) == 0
				? Optional.empty()
				: Optional.of(json.getInt(COMBO_ACTION)),
			Collections.unmodifiableSet(cooldownGroups),
			Duration.ofMillis(json.getLong(RECAST) * 100),
			Duration.ofMillis(json.getLong(CAST) * 100),
			json.getBool(ROLE_ACTION),
			json.getInt(LEVEL),
			cooldownGroups.contains(gcdCdGroup)
		);
	}

}
