package dev.stocky37.xiv.core.enrich;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.model.Ability;
import dev.stocky37.xiv.model.ApiObject;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import javax.inject.Named;

public abstract class MergingEnricher<T extends ApiObject> implements UnaryOperator<T> {
	protected final JsonNode data;
	protected final BiFunction<T, JsonNode, T> merger;

	public MergingEnricher(
		@Named("data.actions") JsonNode data,
		@Named("merger.ability") BiFunction<T, JsonNode, T> merger
	) {
		this.data = data;
		this.merger = merger;
	}

	@Override
	public T apply(T source) {
		final var update = data.get(source.id());
		return update.isMissingNode() ? source : enrich(merger.apply(source, update), update);
	}

	protected abstract T enrich(T ability, JsonNode update);
}
