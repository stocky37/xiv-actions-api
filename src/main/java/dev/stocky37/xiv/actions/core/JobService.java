package dev.stocky37.xiv.actions.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.data.JobConverter;
import dev.stocky37.xiv.actions.xivapi.XivApi;
import io.quarkus.cache.CacheResult;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class JobService {
	private final XivApi xivapi;
	private final RateLimiter rateLimiter;
	private final Function<JsonNode, Job> converter;
	private final UnaryOperator<Job> enricher;

	@Inject
	public JobService(
		@RestClient XivApi xivapi,
		RateLimiter rateLimiter,
		Function<JsonNode, Job> converter,
		UnaryOperator<Job> enricher
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.converter = converter;
		this.enricher = enricher;
	}

	@CacheResult(cacheName = "jobs")
	public List<Job> getAll() {
		rateLimiter.acquire();
		return xivapi.getClassJobs(JobConverter.ALL_FIELDS).Results().parallelStream()
			.filter(x -> x.get(JobConverter.NAME).asText().length() > 0)
			.map(converter)
			.collect(Collectors.toList());
	}

	@CacheResult(cacheName = "jobs")
	public Optional<Job> findByIdentifier(String identifier) {
		return findById(identifier)
			.or(() -> findByName(identifier))
			.or(() -> findByAbbreviation(identifier))
			.map(enricher);
	}

	public Optional<Job> findById(String id) {
		return getAll().stream()
			.filter(j -> j.id().equals(id))
			.findFirst();
	}

	public Optional<Job> findByName(String name) {
		return getAll().stream()
			.filter(j -> j.name().equalsIgnoreCase(name))
			.findFirst();
	}

	public Optional<Job> findByAbbreviation(String abbreviation) {
		return getAll().stream()
			.filter(j -> j.abbreviation().equalsIgnoreCase(abbreviation))
			.findFirst();
	}
}
