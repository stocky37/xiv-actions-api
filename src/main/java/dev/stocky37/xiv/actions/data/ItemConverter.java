package dev.stocky37.xiv.actions.data;

import static dev.stocky37.xiv.actions.data.JobConverter.ABBREV;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.util.Util;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemConverter implements Function<JsonNode, Item> {
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
	public static final String ITEM_ACTION = "ItemAction";
	public static final String BONUS_DURATION = "DataHQ2";

	public static final List<String> ALL_FIELDS = List.of(
		ID,
		NAME,
		ABBREV,
		ICON,
		ICON_HD,
		DESCRIPTION,
		BONUSES,
		COOLDOWN,
		ITEM_ACTION
	);

	private final Util util;

	@Inject
	public ItemConverter(Util util) {this.util = util;}


	@Override
	public Item apply(JsonNode json) {
		return new Item(
			json.get(ID).asText(),
			json.get(NAME).asText(),
			util.prefixUri(json.get(ICON).asText()),
			util.prefixUri(json.get(ICON_HD).asText()),
			json.get(DESCRIPTION).asText(),
			Duration.ofSeconds(json.get(COOLDOWN).asInt() - HQ_CD_REDUCTION),
			Duration.ofSeconds(json.path(ITEM_ACTION).get(BONUS_DURATION).asInt()),
			bonuses(json.path(BONUSES))
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
