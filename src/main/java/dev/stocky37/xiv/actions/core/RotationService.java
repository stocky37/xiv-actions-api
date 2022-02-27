package dev.stocky37.xiv.actions.core;

import dev.stocky37.xiv.actions.config.XivConfig;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.Consumable;
import dev.stocky37.xiv.actions.data.Item;
import dev.stocky37.xiv.actions.data.Rotation;
import dev.stocky37.xiv.actions.data.RotationInput;
import dev.stocky37.xiv.actions.json.RotationBuilder;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

@ApplicationScoped
public class RotationService {
	private final AbilityService actionService;
	private final ItemService itemService;
	private final XivConfig config;

	@Inject
	public RotationService(
		AbilityService abilityService,
		ItemService itemService,
		XivConfig config
	) {
		this.actionService = abilityService;
		this.itemService = itemService;
		this.config = config;
	}

	public Rotation buildRotation(RotationInput input) {
		final List<Action> actions = input.actions().stream()
			.map(id -> {
				if(!id.startsWith("i")) {
					return actionService.findById(id).orElseThrow();
				}
				final var item = itemService.findById(id.substring(1)).orElseThrow();
				if(item.kind() != Item.Kind.CONSUMABLE) {
					throw new BadRequestException("Unsupported item kind: " + item.kind().toString());
				}
				return (Action)item;
			})
			.toList();

		return new RotationBuilder(config).withActions(actions).build();
	}
}


