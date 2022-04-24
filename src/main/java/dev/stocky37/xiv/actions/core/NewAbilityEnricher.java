package dev.stocky37.xiv.actions.core;

import dev.stocky37.xiv.actions.model.Ability;
import java.util.Map;
import java.util.function.UnaryOperator;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class NewAbilityEnricher implements UnaryOperator<Ability> {

	private final Map<String, Ability> data;

	@Inject
	public NewAbilityEnricher(@Named("data.actions") Map<String, Ability> data) {
		this.data = data;
	}


	@Override
	public Ability apply(Ability ability) {
		final var enriched = data.get(ability.id());
		return enriched == null ? ability : merge(ability, enriched);
	}

	public Ability merge(Ability source, Ability update) {
		final var builder = Ability.builder(source);
		if(update.recast() != null) {
			builder.withRecast(update.recast());
		}
		return builder.build();
	}
}
