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
		if(overwrite || !this.containsKey(event.timestamp())) {
			return this.put(event.timestamp(), event);
		}
		final var newTimestamp = event.timestamp().minus(Duration.ofMillis(1));
		System.out.println("new time:" + newTimestamp);
		return this.put(newTimestamp, event);
	}

//	public Entry<Duration, Event> lastGCDEntry() {
//		final var lastGcdKey = lastGCDTime();
//		return lastGcdKey == null
//			? null
//			: new AbstractMap.SimpleImmutableEntry<>(lastGcdKey, get(lastGcdKey));
//	}
//
//	public Duration lastGCDTime() {
//		var curr = lastEntry();
//		while(curr != null) {
//			final var event = curr.getValue();
//			if(event.type() == Event.Type.ACTION && event.isGCD()) {
//				return curr.getKey();
//			}
//			curr = this.lowerEntry(curr.getKey());
//		}
//		return null;
//	}
//
//	public Duration lastOGCDTime() {
//		var curr = lastEntry();
//		while(curr != null) {
//			final var event = curr.getValue();
//			if(event.type() == Event.Type.ACTION && !event.isGCD()) {
//				return curr.getKey();
//			}
//			curr = this.lowerEntry(curr.getKey());
//		}
//		return null;
//	}
}
