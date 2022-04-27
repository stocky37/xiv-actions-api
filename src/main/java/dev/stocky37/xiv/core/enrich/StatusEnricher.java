package dev.stocky37.xiv.core.enrich;

import dev.stocky37.xiv.model.Status;
import dev.stocky37.xiv.model.transform.StatusMerger;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class StatusEnricher implements UnaryOperator<Status> {

	private final Map<String, Status> data;
	private final BinaryOperator<Status> merger;

	@Inject
	public StatusEnricher(@Named("data.status") Map<String, Status> data, StatusMerger merger) {
		this.data = data;
		this.merger = merger;
	}

	@Override
	public Status apply(Status source) {
		final var updated = data.get(source.id());
		return updated == null ? source : merger.apply(source, updated);
	}
}
