package dev.stocky37.xiv.core.enrich;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.model.ApiObject;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public abstract class MergingEnricher<T extends ApiObject> implements UnaryOperator<T> {
	protected final JsonNode data;
	protected final BiFunction<T, JsonNode, T> merger;

	public MergingEnricher(JsonNode data, BiFunction<T, JsonNode, T> merger) {
		this.data = data;
		this.merger = merger;
	}

	@Override
	public T apply(T source) {
		final var update = data.isNull() ? null : data.get(source.id());
		final var merged = update == null ? source : merger.apply(source, update);
		return enrich(merged, update);
	}

	protected abstract T enrich(T ability, JsonNode update);
}
