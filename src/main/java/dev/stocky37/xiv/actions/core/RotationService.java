package dev.stocky37.xiv.actions.core;

import com.ibm.asyncutil.util.Either;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.Item;
import dev.stocky37.xiv.actions.data.Rotation;
import dev.stocky37.xiv.actions.data.RotationInput;
import dev.stocky37.xiv.actions.json.RotationBuilder;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RotationService {
	private final ActionService actionService;
	private final ItemService itemService;

	@Inject
	public RotationService(
		ActionService actionService,
		ItemService itemService
	) {
		this.actionService = actionService;
		this.itemService = itemService;
	}

	public Rotation buildRotation(RotationInput input) {
		final List<Either<Action, Item>> actions = input.actions().stream()
			.map(id -> {
				if(id.startsWith("i")) {
					return itemService.findById(id.substring(1));
				} else {
					return actionService.findById(id);
				}
			})
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(opt -> (opt instanceof Action)
				? Either.<Action, Item>left((Action) opt) : Either.<Action, Item>right((Item) opt))
			.toList();

		return new RotationBuilder(actions).build();
	}
}


