package dev.stocky37.xiv.actions.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import dev.stocky37.xiv.actions.data.Action;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class ActionDeserializer extends JsonNodeDeserializer<Action> {

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

	@Inject
	public ActionDeserializer(
		@ConfigProperty(name = "gcd-cd-group") int gcdCdGroup,
		@ConfigProperty(name = "xivapi/mp-rest/uri") String baseUri
	) {
		super(Action.class, baseUri);
		this.gcdCdGroup = gcdCdGroup;
	}

	@Override
	public Action apply(JsonNode node) {
		final Set<Integer> cooldownGroups = Sets.newHashSet(
			get(node, COOLDOWN_GROUP).asInt(),
			get(node, COOLDOWN_GROUP_ALT).asInt()
		);

		return new Action(
			get(node, ID).asText(),
			get(node, NAME).asText(),
			get(node, CATEGORY).asText(),
			get(node, DESCRIPTION).asText(),
			getUri(node, ICON),
			getUri(node, ICON_HD),
			get(node, COMBO_ACTION).asInt() == 0
				? Optional.empty()
				: Optional.of(get(node, COMBO_ACTION).asInt()),
			Collections.unmodifiableSet(cooldownGroups),
			Duration.ofMillis(get(node, RECAST).asLong() * 100),
			Duration.ofMillis(get(node, CAST).asLong() * 100),
			get(node, ROLE_ACTION).asBoolean(),
			get(node, LEVEL).asInt(),
			cooldownGroups.contains(gcdCdGroup)
		);
	}

	@Override
	public Action deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		return apply(p.getCodec().readTree(p));
	}
}
