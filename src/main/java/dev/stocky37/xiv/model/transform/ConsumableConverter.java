package dev.stocky37.xiv.model.transform;

import dev.stocky37.xiv.model.Attribute;
import dev.stocky37.xiv.model.Consumable;
import dev.stocky37.xiv.util.Util;
import dev.stocky37.xiv.xivapi.json.XivConsumable;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ConsumableConverter implements Function<XivConsumable, Consumable> {
	private static final int HQ_CD_REDUCTION = 30;

	private final Util util;

	@Inject
	public ConsumableConverter(Util util) {
		this.util = util;
	}

	@Override
	public Consumable apply(XivConsumable consumable) {
		return new Consumable(
			consumable.ID().toString(),
			consumable.Name(),
			util.prefixXivApi(consumable.Icon()),
			util.prefixXivApi(consumable.IconHD()),
			consumable.Description(),
			Duration.ofSeconds(consumable.CooldownS() - HQ_CD_REDUCTION),
			Duration.ofSeconds(consumable.ItemAction().DataHQ2()),
			bonuses(consumable.Bonuses())
		);
	}

	private List<Consumable.Bonus> bonuses(Map<String, XivConsumable.Bonus> xivBonuses) {
		return xivBonuses.entrySet()
			.stream()
			.map((e) -> new Consumable.Bonus(
				Attribute.fromString(e.getKey()),
				e.getValue().ValueHQ(),
				e.getValue().MaxHQ()
			))
			.toList();
	}
}
