package dev.stocky37.xiv.test;

import dev.stocky37.xiv.config.DataLoader;
import dev.stocky37.xiv.model.Ability;
import dev.stocky37.xiv.model.Consumable;
import dev.stocky37.xiv.model.Status;
import dev.stocky37.xiv.util.Util;
import javax.inject.Named;
import javax.inject.Singleton;

public class TestData {

	@Singleton
	@Named("abilities.shadow-of-death")
	public static Ability shadowOfDeath(Util util) {
		return util.fromJsonNode(DataLoader.loadData("fixtures/abilities/shadow-of-death.yml"), Ability.class);
	}

	@Singleton
	@Named("statuses.deaths-design")
	public static Status deathsDesign(Util util) {
		return util.fromJsonNode(DataLoader.loadData("fixtures/statuses/deaths-design.yml"), Status.class);
	}

	@Singleton
	@Named("items.tincture")
	public static Consumable tinctureOfStrength(Util util) {
		return util.fromJsonNode(DataLoader.loadData("fixtures/items/tincture.yml"), Consumable.class);
	}
}
