package dev.stocky37.xiv.core.enrich;

import dev.stocky37.xiv.model.Ability;
import dev.stocky37.xiv.model.transform.AbilityMerger;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class AbilityEnricher implements UnaryOperator<Ability> {

	private final Map<String, Ability> data;
	private final BinaryOperator<Ability> merger;

	@Inject
	public AbilityEnricher(@Named("data.actions") Map<String, Ability> data, AbilityMerger merger) {
		this.data = data;
		this.merger = merger;
	}

	@Override
	public Ability apply(Ability source) {
		final var updated = data.get(source.id());
		return updated == null ? source : merger.apply(source, updated);
	}
}
