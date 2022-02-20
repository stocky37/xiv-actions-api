package dev.stocky37.xiv.actions.core;

import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.Rotation;
import dev.stocky37.xiv.actions.data.RotationInput;
import dev.stocky37.xiv.actions.json.RotationBuilder;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RotationService {
	private final AbilityService actionService;
	private final ItemService itemService;

	@Inject
	public RotationService(
		AbilityService abilityService,
		ItemService itemService
	) {
		this.actionService = abilityService;
		this.itemService = itemService;
	}

	public Rotation buildRotation(RotationInput input) {
		final List<? extends Action> actions = input.actions().stream()
			.map(id -> (id.startsWith("i")
				? itemService.findById(id.substring(1)).orElseThrow()
				: actionService.findById(id).orElseThrow()))
			.toList();

		return new RotationBuilder(actions).build();
	}
}


