package dev.stocky37.xiv.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import dev.stocky37.xiv.api.json.Views;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Optional;
import java.util.function.UnaryOperator;

public record Status(
	// ApiObject
	String id,
	String name,
	URI icon,

	// Status
	@JsonView(Views.Standard.class) URI hdIcon,
	@JsonView(Views.Standard.class) Duration duration,
	@JsonView(Views.Standard.class) Optional<Duration> maxDuration,
	@JsonView(Views.Standard.class) Collection<Effect<?>> effects
) implements ApiObject {

	public enum Type {
		STAT, DAMAGE
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
	@JsonSubTypes({
		@JsonSubTypes.Type(value = DamageModifier.class),
		@JsonSubTypes.Type(value = StatModifier.class)
	})
	public interface Effect<T> extends UnaryOperator<T> {
		Type type();

		T modify(T t);

		@Override
		default T apply(T t) {
			return t == null ? null : modify(t);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(Status status) {
		return new Builder(status);
	}

	public static class Builder {
		private String id;
		private String name;
		private URI icon;
		private URI hdIcon;
		private Duration duration = Duration.ZERO;
		private Optional<Duration> maxDuration = Optional.empty();
		private Collection<Effect<?>> effects = new ArrayDeque<>();

		private Builder() {}

		private Builder(Status status) {
			this.id = status.id;
			this.name = status.name;
			this.icon = status.icon;
			this.hdIcon = status.hdIcon;
			this.duration = status.duration;
			this.maxDuration = status.maxDuration;
			this.effects = status.effects;
		}

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder icon(URI icon) {
			this.icon = icon;
			return this;
		}

		public Builder hdIcon(URI hdIcon) {
			this.hdIcon = hdIcon;
			return this;
		}

		public Builder duration(Duration duration) {
			this.duration = duration;
			return this;
		}

		public Builder maxDuration(Duration maxDuration) {
			this.maxDuration = Optional.ofNullable(maxDuration);
			return this;
		}

		public Builder effects(Collection<Effect<?>> effects) {
			this.effects = effects;
			return this;
		}

		public Status build() {
			return new Status(id, name, icon, hdIcon, duration, maxDuration, effects);
		}
	}

}
