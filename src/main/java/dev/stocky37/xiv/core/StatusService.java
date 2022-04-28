package dev.stocky37.xiv.core;

import dev.stocky37.xiv.core.enrich.StatusEnricher;
import dev.stocky37.xiv.model.Status;
import dev.stocky37.xiv.model.transform.StatusConverter;
import dev.stocky37.xiv.xivapi.XivApiClient;
import dev.stocky37.xiv.xivapi.json.XivStatus;
import io.quarkus.cache.CacheResult;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StatusService {

	private final XivApiClient xivapi;
	private final Function<XivStatus, Status> converter;
	private final UnaryOperator<Status> enricher;

	@Inject
	public StatusService(XivApiClient xivapi, StatusConverter converter, StatusEnricher enricher) {
		this.xivapi = xivapi;
		this.converter = converter;
		this.enricher = enricher;
	}

	@CacheResult(cacheName = "statuses")
	public Optional<Status> findById(String id) {
		return xivapi.getStatus(id).map(converter).map(enricher);
	}
}
