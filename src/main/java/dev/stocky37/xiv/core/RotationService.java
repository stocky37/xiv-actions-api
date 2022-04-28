package dev.stocky37.xiv.core;

import dev.stocky37.xiv.api.json.RotationInput;
import dev.stocky37.xiv.config.XivConfig;
import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.model.Delay;
import dev.stocky37.xiv.model.Item;
import dev.stocky37.xiv.model.Rotation;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

@Singleton
public class RotationService {
	private final XivConfig config;
	private final AbilityService abilities;
	private final ItemService items;
	private final JobService jobs;

	@Inject
	public RotationService(
		XivConfig config,
		AbilityService abilities,
		ItemService items,
		JobService jobs
	) {
		this.config = config;
		this.abilities = abilities;
		this.items = items;
		this.jobs = jobs;
	}

	public Rotation buildRotation(RotationInput input) {
		final var builder = new RotationBuilder(
			config,
			jobs.findByIdentifier(input.job()).orElseThrow(NotFoundException::new),
			input.stats()
		);
		input.rotation().forEach(str -> builder.append(handleAction(str)));
		return builder.build();
	}

	private Action handleAction(RotationInput.ActionRef input) {
		return switch(input.type()) {
			case ABILITY -> handleAbility(input);
			case ITEM -> handleItem(input);
			case DELAY -> handleDelay(input);
		};
	}

	private Action handleAbility(RotationInput.ActionRef input) {
		return abilities.findById(input.id()
				.orElseThrow(() -> new BadRequestException("No id present for ability action")))
			.orElseThrow(() -> new BadRequestException("No ability found with id: " + input.id()));
	}

	private Action handleItem(RotationInput.ActionRef input) {
		final var item = items
			.findConsumableById(input.id()
				.orElseThrow(() -> new BadRequestException("No id present for item action")))
			.orElseThrow(() -> new BadRequestException("No item found with id: " + input.id()));
		if(item.kind() != Item.Kind.CONSUMABLE) {
			throw new BadRequestException("Unsupported item kind: " + item.kind().toString());
		}
		return (Action) item;
	}

	private Action handleDelay(RotationInput.ActionRef input) {
		return new Delay(input.length().orElse(config.animationLock()));
	}
}


