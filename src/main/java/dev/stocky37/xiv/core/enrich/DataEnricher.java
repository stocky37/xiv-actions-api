package dev.stocky37.xiv.core.enrich;

import dev.stocky37.xiv.model.ApiObject;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public abstract class DataEnricher<T extends ApiObject> implements UnaryOperator<T> {

	private final Map<String, T> data;
	private final BinaryOperator<T> merger;

	public DataEnricher(Map<String, T> data, BinaryOperator<T> merger) {
		this.data = data;
		this.merger = merger;
	}

	@Override
	public T apply(T source) {
		final var updated = data.get(source.id());
		return updated == null ? source : merger.apply(source, updated);
	}
}
