package dev.stocky37.xiv.core.enrich;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.core.StatusService;
import dev.stocky37.xiv.model.Ability;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class AbilityEnricher extends MergingEnricher<Ability> {
	private final StatusService statusService;

	@Inject
	public AbilityEnricher(
		@Named("abilities.data") JsonNode data,
		@Named("abilities.merge") BiFunction<Ability, JsonNode, Ability> merger,
		StatusService statusService
	) {
		super(data, merger);
		this.statusService = statusService;
	}

	@Override
	protected Ability enrich(Ability ability, JsonNode update) {
		final var builder = Ability.builder(ability);
		if(update.has("statusIds")) {
			builder.withStatuses(
				StreamSupport.stream(update.get("statusIds").spliterator(), false)
					.map((n) -> statusService.findById(n.asText()))
					.filter(Optional::isPresent)
					.map(Optional::get)
					.toList()
			);
		}
		return builder.build();
	}

}
