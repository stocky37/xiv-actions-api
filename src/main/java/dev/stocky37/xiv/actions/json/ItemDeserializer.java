package dev.stocky37.xiv.actions.json;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.data.Attribute;
import dev.stocky37.xiv.actions.data.Item;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class ItemDeserializer extends JsonNodeDeserializer<Item> {
	private static final int HQ_CD_REDUCTION = 30;

	public static final String ID = "ID";
	public static final String NAME = "Name";
	public static final String ICON = "Icon";
	public static final String ICON_HD = "IconHD";
	public static final String DESCRIPTION = "Description";
	public static final String BONUSES = "Bonuses";
	public static final String BONUS_MAX = "MaxHQ";
	public static final String BONUS_VALUE = "ValueHQ";
	public static final String COOLDOWN = "CooldownS";
	public static final String BONUS_DURATION = "ItemAction.DataHQ2";

	public static final List<String> ALL_FIELDS =
		List.of(ID, NAME, ICON, ICON_HD, DESCRIPTION, BONUSES, COOLDOWN, BONUS_DURATION);

	@Inject
	public ItemDeserializer(@ConfigProperty(name = "xivapi/mp-rest/uri") String baseUri) {
		super(Item.class, baseUri);
	}

	@Override
	public Item apply(JsonNodeWrapper node) {
		return new Item(
			node.get(ID).asText(),
			node.get(NAME).asText(),
			getUri(node, ICON),
			getUri(node, ICON_HD),
			node.get(DESCRIPTION).asText(),
			Duration.ofSeconds(node.get(COOLDOWN).asInt() - HQ_CD_REDUCTION),
			Duration.ofSeconds(node.get(BONUS_DURATION).asInt()),
			bonuses(node.get(BONUSES))
		);
	}

	private List<Item.Bonus> bonuses(JsonNode json) {
		final var bonuses = new ArrayList<Item.Bonus>();
		for(final var it = json.fields(); it.hasNext(); ) {
			final Map.Entry<String, JsonNode> entry = it.next();
			bonuses.add(new Item.Bonus(
				Attribute.fromString(entry.getKey()),
				entry.getValue().get(BONUS_VALUE).asInt(),
				entry.getValue().get(BONUS_MAX).asInt()
			));
		}
		return bonuses;
	}
}
