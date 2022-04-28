package dev.stocky37.xiv.core;

import dev.stocky37.xiv.model.Ability;
import dev.stocky37.xiv.model.Attribute;
import dev.stocky37.xiv.model.Consumable;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.inject.Named;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ItemServiceTest implements WithAssertions {

	@Inject
	ItemService items;

	@Inject
	@Named("items.tincture")
	Consumable tincture;

	@Test
	void findConsumableById() {
		assertThat(items.findConsumableById("36109").orElseThrow()).isEqualTo(tincture);
	}

	@Test
	void findPotionsForAttribute() {
		assertThat(items.findPotionsForAttribute(Attribute.STRENGTH)).hasSize(16);
		assertThat(items.findPotionsForAttribute(Attribute.STRENGTH)).contains(tincture);
	}
}
