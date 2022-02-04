package dev.stocky37.xiv.actions.core;

import com.ibm.asyncutil.util.Either;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.Item;
import dev.stocky37.xiv.actions.data.Rotation;
import dev.stocky37.xiv.actions.data.RotationAction;
import dev.stocky37.xiv.actions.data.RotationInput;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RotationService {

	public static final Duration OGCD_DELAY = Duration.ofMillis(700);
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
		final var actions = input.actions().stream()
			.map(id -> {
				if(id.startsWith("i")) {
					return itemService.findById(id.substring(1));
				} else {
					return actionService.findById(id);
				}
			})
			.filter(Optional::isPresent)
			.map(Optional::get)
			.toList();
		return new Rotation(buildTimeline(actions));
	}

	public List<RotationAction> buildTimeline(List<? extends Record> actions) {
		final List<RotationAction> rotation = new ArrayList<>(actions.size());

		Duration nextGcd = Duration.ZERO;
		Duration nextOGcd = nextGcd.plus(OGCD_DELAY);

		int gcdCount = 1;
		for(final var action : actions) {
			final Either<Action, Item> either = (action instanceof Action)
				? Either.left((Action) action) : Either.right((Item) action);

			if(either.fold(Action::onGCD, Item::onGCD)) {
				rotation.add(new RotationAction(
					either.left(),
					either.right(),
					nextGcd,
					Optional.of(gcdCount++)
				));

				nextOGcd = nextGcd.plus(OGCD_DELAY);
				nextGcd = nextGcd.plus(either.fold(Action::recast, Item::recast));

			} else {
				rotation.add(new RotationAction(
					either.left(),
					either.right(),
					nextOGcd,
					Optional.empty()
				));
				
				nextOGcd = nextOGcd.plus(OGCD_DELAY);
				if(nextOGcd.compareTo(nextGcd) > 0) {
					nextGcd = nextOGcd;
				}
			}
		}
		return rotation;
	}
}
