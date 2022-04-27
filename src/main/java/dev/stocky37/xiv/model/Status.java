package dev.stocky37.xiv.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.net.URI;
import java.util.Collection;
import java.util.function.UnaryOperator;

public record Status(
	String id,
	String name,
	URI icon,
	URI hdIcon,
	String description,
	Collection<Effect<?>> effects
) implements ApiObject {

	enum Type {
		STAT, DAMAGE
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
	@JsonSubTypes({
		@JsonSubTypes.Type(value = DamageMultiplier.class),
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

}
