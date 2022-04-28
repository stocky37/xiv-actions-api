package dev.stocky37.xiv.model.transform;

import dev.stocky37.xiv.core.StatusService;
import dev.stocky37.xiv.model.Attribute;
import dev.stocky37.xiv.model.Consumable;
import dev.stocky37.xiv.model.StatModifier;
import dev.stocky37.xiv.model.Status;
import dev.stocky37.xiv.util.Util;
import dev.stocky37.xiv.xivapi.json.XivConsumable;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ConsumableConverter implements Function<XivConsumable, Consumable> {

	public static final String MEDICATED_ID = "49";
	private static final int HQ_CD_REDUCTION = 30;

	private final Util util;

	private final StatusService statuses;

	@Inject
	public ConsumableConverter(Util util, StatusService statuses) {
		this.util = util;
		this.statuses = statuses;
	}

	@Override
	public Consumable apply(XivConsumable consumable) {
		return new Consumable(
			consumable.ID().toString(),
			consumable.Name(),
			util.prefixXivApi(consumable.Icon()),
			util.prefixXivApi(consumable.IconHD()),
			Duration.ofSeconds(consumable.CooldownS() - HQ_CD_REDUCTION),
			effects(consumable.Bonuses(), Duration.ofSeconds(consumable.ItemAction().DataHQ2()))
		);
	}

	private Status getMedicatedStatus() {
		return statuses.findById(MEDICATED_ID).orElseThrow();
	}

	private StatModifier statmod(Attribute attribute, int value, int max) {
		return new StatModifier(attribute, value / 100d + 1, Optional.of(max));
	}


	private Status medicatedStatus(Duration duration, StatModifier effect) {
		return Status.builder(getMedicatedStatus())
			.duration(duration)
			.effects(List.of(effect))
			.build();
	}

	// todo: stop assuming all consumables are potions - will probably need to handle food eventually
	private List<Status> effects(Map<String, XivConsumable.Bonus> xivBonuses, Duration duration) {
		return xivBonuses.entrySet()
			.stream()
			.map((e) -> medicatedStatus(
				duration,
				statmod(
					Attribute.fromString(e.getKey()),
					e.getValue().ValueHQ(),
					e.getValue().MaxHQ()
				)
			)).toList();
	}
}
