package dev.stocky37.xiv.model.transform;

import dev.stocky37.xiv.model.Ability;
import java.util.function.BinaryOperator;
import javax.inject.Singleton;

@Singleton
public class AbilityMerger implements BinaryOperator<Ability> {
	@Override
	public Ability apply(Ability source, Ability update) {
		final var builder = Ability.builder(source);
		if(update.recast() != null) {
			builder.withRecast(update.recast());
		}
		return builder.build();
	}
}
