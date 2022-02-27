package dev.stocky37.xiv.actions.json;

import com.google.common.collect.Lists;
import dev.stocky37.xiv.actions.config.XivConfig;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.RotationEffect;
import dev.stocky37.xiv.actions.data.Rotation;
import dev.stocky37.xiv.actions.data.RotationAction;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RotationBuilder {
	private final XivConfig config;
	private final List<Action> actions = new ArrayList<>();
	private final List<RotationEffect> rotationEffects = new ArrayList<>();
	private Duration nextGcd = Duration.ZERO;
	private Duration nextOGcd = Duration.ZERO;
	private int gcdCount = 0;

	public RotationBuilder(XivConfig config) {
		this.config = config;
	}

	public RotationBuilder append(Action action) {
		this.actions.add(action);
		return this;
	}

	public RotationBuilder withActions(List<Action> actions) {
		this.actions.addAll(actions);
		return this;
	}

	public Rotation build() {
		final List<RotationAction> timeline = actions.stream().map(this::buildAction).toList();
		return new Rotation(timeline);
	}

	private RotationAction buildAction(Action action) {
		return action.onGCD() ? handleGcd(action) : handleOGcd(action);
	}

	private RotationAction handleGcd(Action action) {
		removeEffects(nextGcd);
		addEffects(action, nextGcd);

		final var rotationAction = new RotationAction(
			action,
			nextGcd,
			Optional.of(++gcdCount),
			Lists.newArrayList(rotationEffects)
		);

		nextOGcd = nextGcd.plus(animationLock(action));
		nextGcd = nextGcd.plus(action.recast());

		return rotationAction;
	}

	private RotationAction handleOGcd(Action action) {
		removeEffects(nextOGcd);
		addEffects(action, nextOGcd);

		final var rotationAction = new RotationAction(
			action,
			nextOGcd,
			Optional.empty(),
			Lists.newArrayList(rotationEffects)
		);

		nextOGcd = nextOGcd.plus(animationLock(action));
		if(nextOGcd.compareTo(nextGcd) > 0) {
			nextGcd = nextOGcd;
		}
		return rotationAction;
	}

	private Duration animationLock(Action action) {
		return action.animationLock().orElse(config.animationLock()).plus(config.ping());
	}

	private void addEffects(Action action, Duration start) {
		action.effects().forEach(e -> rotationEffects.add(new RotationEffect(
			e,
			start,
			start.plus(e.length())
		)));
	}

	private void removeEffects(Duration start) {
		final List<RotationEffect> finishedEffects = new ArrayList<>();
		rotationEffects.stream()
			.filter(item -> item.end().compareTo(start) < 0)
			.forEach(finishedEffects::add);
		rotationEffects.removeAll(finishedEffects);
	}
}
