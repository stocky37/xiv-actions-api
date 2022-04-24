package dev.stocky37.xiv.actions.json;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.model.Attribute;
import dev.stocky37.xiv.actions.model.Consumable;
import dev.stocky37.xiv.actions.model.Item;
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
	public static final String KIND = "ItemKind.ID";

	public static final List<String> ALL_FIELDS =
		List.of(ID, NAME, ICON, ICON_HD, DESCRIPTION, BONUSES, COOLDOWN, BONUS_DURATION, KIND);

	@Inject
	public ItemDeserializer(@ConfigProperty(name = "xivapi/mp-rest/uri") String baseUri) {
		super(Consumable.class, baseUri);
	}

	@Override
	public Item apply(JsonNodeWrapper json) {
		return switch(json.get(KIND).asInt()) {
			case 5 -> deserializeConsumable(json);
			case 1, 2, 3, 4, 6, 7 -> throw new RuntimeException("Unsupported item kind");
			default -> throw new RuntimeException("Unknown item kind");
		};
	}

	private Consumable deserializeConsumable(JsonNodeWrapper json) {
		return new Consumable(
			json.get(ID).asText(),
			json.get(NAME).asText(),
			getUri(json, ICON),
			getUri(json, ICON_HD),
			json.get(DESCRIPTION).asText(),
			Duration.ofSeconds(json.get(COOLDOWN).asInt() - HQ_CD_REDUCTION),
			Duration.ofSeconds(json.get(BONUS_DURATION).asInt()),
			bonuses(json.get(BONUSES))
		);
	}

	private List<Consumable.Bonus> bonuses(JsonNode json) {
		final var bonuses = new ArrayList<Consumable.Bonus>();
		for(final var it = json.fields(); it.hasNext(); ) {
			final Map.Entry<String, JsonNode> entry = it.next();
			bonuses.add(new Consumable.Bonus(
				Attribute.fromString(entry.getKey()),
				entry.getValue().get(BONUS_VALUE).asInt(),
				entry.getValue().get(BONUS_MAX).asInt()
			));
		}
		return bonuses;
	}
}
