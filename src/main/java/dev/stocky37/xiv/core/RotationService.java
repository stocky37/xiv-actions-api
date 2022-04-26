package dev.stocky37.xiv.core;

import dev.stocky37.xiv.api.json.RotationInput;
import dev.stocky37.xiv.config.XivConfig;
import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.model.Attribute;
import dev.stocky37.xiv.model.Delay;
import dev.stocky37.xiv.model.Item;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.Rotation;
import dev.stocky37.xiv.model.Stats;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

@ApplicationScoped
public class RotationService {
	private final AbilityService abilityService;
	private final ItemService itemService;
	private final XivConfig config;

	private static final Job RPR = Job.builder()
		.withId("39")
		.withName("reaper")
		.withAbbreviation("RPR")
		.withType(Job.Type.JOB)
		.withPrimaryStat(Attribute.STRENGTH)
		.withCategory(Job.Category.DOW)
		.withRole(Job.Role.MELEE_DPS)
		.withLimited(false)
		.build();

	private static final Stats stats = Stats.builder()
		.physicalDamage(115)
		.strength(2575)
		.skillSpeed(436)
		.determination(1846)
		.crit(2281)
		.directHit(1199)
		.build();

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
		final var builder = new RotationBuilder(config, RPR, stats);
		input.rotation().forEach(str -> builder.append(handleAction(str)));
		return builder.build();
	}

	private Action handleAction(RotationInput.Action input) {
		return switch(input.type()) {
			case ABILITY -> handleAbility(input);
			case ITEM -> handleItem(input);
			case DELAY -> handleDelay(input);
		};
	}

	private Action handleAbility(RotationInput.Action input) {
		return abilityService.findById(input.id()
				.orElseThrow(() -> new BadRequestException("No id present for ability action")))
			.orElseThrow(() -> new BadRequestException("No ability found with id: " + input.id()));
	}

	private Action handleItem(RotationInput.Action input) {
		final var item = itemService
			.findConsumableById(input.id()
				.orElseThrow(() -> new BadRequestException("No id present for item action")))
			.orElseThrow(() -> new BadRequestException("No item found with id: " + input.id()));
		if(item.kind() != Item.Kind.CONSUMABLE) {
			throw new BadRequestException("Unsupported item kind: " + item.kind().toString());
		}
		return (Action) item;
	}

	private Action handleDelay(RotationInput.Action input) {
		return new Delay(input.length().orElse(config.animationLock()));
	}
}


