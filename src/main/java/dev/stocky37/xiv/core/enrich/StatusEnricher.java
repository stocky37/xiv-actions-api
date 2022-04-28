package dev.stocky37.xiv.core.enrich;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.model.Status;
import java.util.function.BiFunction;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class StatusEnricher extends MergingEnricher<Status> {

	@Inject
	public StatusEnricher(
		@Named("statuses.data") JsonNode data,
		@Named("statuses.merge") BiFunction<Status, JsonNode, Status> merger
	) {
		super(data, merger);
	}

	@Override
	protected Status enrich(Status ability, JsonNode update) {
		return ability;
	}
}
