package dev.stocky37.xiv.core;

import static dev.stocky37.xiv.util.Util.slugify;

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
		return this.put(event.timestamp(), event);
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
