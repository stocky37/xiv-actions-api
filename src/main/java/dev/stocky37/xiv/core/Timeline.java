package dev.stocky37.xiv.core;

import static dev.stocky37.xiv.util.Util.slugify;

import com.google.common.collect.ForwardingNavigableMap;
import dev.stocky37.xiv.model.Action;
import dev.stocky37.xiv.model.ActiveStatus;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

public class Timeline extends ForwardingNavigableMap<Duration, Timeline.Event> {

	public static Event autoAttackEvent(Duration timestamp, long damage, Collection<ActiveStatus> status) {
		return new Event(Event.Type.AUTO_ATTACK, timestamp, damage, Optional.empty(), status);
	}

	public static Event actionEvent(Duration timestamp, long damage, Collection<ActiveStatus> status, Action action) {
		return new Event(Event.Type.ACTION, timestamp, damage, Optional.of(action), status);
	}

	public record Event(
		Type type,
		Duration timestamp,
		long damage,
		Optional<Action> action,
		Collection<ActiveStatus> statusEffects
	) {

		enum Type {
			ACTION, AUTO_ATTACK;

			@Override
			public String toString() {
				return slugify(name());
			}
		}

		boolean isGCD() {
			return action().map(Action::onGCD).orElse(false);
		}
	}

	private final TreeMap<Duration, Event> delegate = new TreeMap<>();

	@Override
	protected NavigableMap<Duration, Event> delegate() {
		return delegate;
	}

	public Event addEvent(Event event) {
		return addEvent(event, false);
	}

	public Event addEvent(Event event, boolean overwrite) {
		return overwrite || !this.containsKey(event.timestamp())
			? this.put(event.timestamp(), event)
			: this.put(event.timestamp().minus(Duration.ofMillis(1)), event);
	}

	public Map.Entry<Duration, Event> lastAction() {
		return this.lastEntry();
	}

	public long totalDamage() {
		return values().stream()
			.map(Timeline.Event::damage)
			.reduce(Long::sum)
			.orElse(0L);
	}

	public Duration totalTime() {
		return lastEntry().getKey();
	}

	public double dps() {
		return totalDamage() / (double) totalTime().toMillis() * 1000;
	}

	public Map.Entry<Duration, Event> lastGcd() {
		var entry = this.lastAction();
		while(entry != null && !entry.getValue().isGCD()) {
			entry = this.lowerEntry(entry.getKey());
		}
		return entry;
	}

	public Map.Entry<Duration, Event> lastOgcd() {
		var entry = this.lastAction();
		while(entry != null && entry.getValue().isGCD()) {
			entry = this.lowerEntry(entry.getKey());
		}
		return entry;
	}
}
