package dev.stocky37.xiv.actions.core;

import dev.stocky37.xiv.actions.config.XivConfig;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.Delay;
import dev.stocky37.xiv.actions.data.Item;
import dev.stocky37.xiv.actions.data.Rotation;
import dev.stocky37.xiv.actions.data.RotationInput;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

@ApplicationScoped
public class RotationService {
	private final AbilityService abilityService;
	private final ItemService itemService;
	private final XivConfig config;

	@Inject
	public RotationService(
		AbilityService abilityService,
		ItemService itemService,
		XivConfig config
	) {
		this.abilityService = abilityService;
		this.itemService = itemService;
		this.config = config;
	}

	public Rotation buildRotation(RotationInput input) {
		final var builder = new RotationBuilder(config);
		input.actions().forEach(str -> builder.append(parseAction(str)));
		return builder.build();
	}

	private Action parseAction(String str) {
		// standard delay for nulls for now
		if(str == null) {
			return new Delay(config.animationLock());
		}

		// starts with i, then it's an item
		if(str.startsWith("i")) {
			final var id = str.substring(1);
			final var item = itemService.findById(id)
				.orElseThrow(() -> new BadRequestException("No item found with id: " + id));
			if(item.kind() != Item.Kind.CONSUMABLE) {
				throw new BadRequestException("Unsupported item kind: " + item.kind().toString());
			}
			return (Action) item;
		}

		// otherwise, it's an ability
		return abilityService.findById(str)
			.orElseThrow(() -> new BadRequestException("No ability found with id: " + str));
	}
}


