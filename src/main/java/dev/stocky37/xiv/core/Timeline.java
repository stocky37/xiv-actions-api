package dev.stocky37.xiv.core;

import static dev.stocky37.xiv.util.Util.slugify;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ForwardingNavigableMap;
import java.time.Duration;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Timeline extends ForwardingNavigableMap<Duration, Timeline.Event> {

	public interface Event {
		enum Type {
			ACTION, AUTO_ATTACK;

			@Override
			public String toString() {
				return slugify(name());
			}
		}

		@JsonProperty
		Type type();

		Duration timestamp();

		boolean isGCD();

		long damage();
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
}
