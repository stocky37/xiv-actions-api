package dev.stocky37.xiv.actions.core;

import dev.stocky37.xiv.actions.data.Action;
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

	@Inject
	public RotationService(ActionService actionService) {
		this.actionService = actionService;
	}

	public Rotation buildRotation(RotationInput input) {
		final var actions = input.actions().stream()
			.map(actionService::findById)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.toList();
		return new Rotation(buildTimeline(actions));
	}

	public List<RotationAction> buildTimeline(List<Action> actions) {
		final List<RotationAction> rotation = new ArrayList<>(actions.size());

		Duration nextGcd = Duration.ZERO;
		Duration nextOGcd = nextGcd.plus(OGCD_DELAY);

		int gcdCount = 1;
		for(final var action : actions) {
			if(action.onGCD()) {
				rotation.add(new RotationAction(action, nextGcd, Optional.of(gcdCount++)));
				nextOGcd = nextGcd.plus(OGCD_DELAY);
				nextGcd = nextGcd.plus(action.recast());
			} else {
				rotation.add(new RotationAction(action, nextOGcd, Optional.empty()));
				nextOGcd = nextOGcd.plus(OGCD_DELAY);
				if(nextOGcd.compareTo(nextGcd) > 0) {
					nextGcd = nextOGcd;
				}
			}
		}
		return rotation;
	}
}
