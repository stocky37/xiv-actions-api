package dev.stocky37.xiv.core;

import dev.stocky37.xiv.core.enrich.JobEnricher;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.transform.JobConverter;
import dev.stocky37.xiv.util.Util;
import dev.stocky37.xiv.xivapi.XivApiClient;
import dev.stocky37.xiv.xivapi.json.XivClassJob;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheResult;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import javax.inject.Singleton;

@Singleton
public class JobService {
	private final XivApiClient xivapi;
	private final UnaryOperator<Job> enricher;
	private final Function<XivClassJob, Job> converter;

	public JobService(XivApiClient xivapi, JobEnricher enricher, JobConverter converter) {
		this.xivapi = xivapi;
		this.enricher = enricher;
		this.converter = converter;
	}

	public List<Job> getAll() {
		return getAll(Optional.empty());
	}

	@CacheResult(cacheName = "jobs")
	public List<Job> getAll(Optional<Job.Type> type) {
		return xivapi.getJobs()
			.stream()
			.map(converter)
			.filter(j -> type.isEmpty() || j.type() == type.get())
			.toList();
	}

	@CacheResult(cacheName = "jobs")
	public Optional<Job> findByIdentifier(String identifier) {
		return findById(identifier)
			.or(() -> findByName(identifier))
			.or(() -> findByAbbreviation(identifier))
			.map(enricher);
	}

	private Optional<Job> findById(String id) {
		return getAll().stream()
			.filter(j -> j.id().equals(id))
			.findFirst();
	}

	private Optional<Job> findByName(String name) {
		return getAll().stream()
			.filter(j -> j.slug().equals(Util.slugify(name)))
			.findFirst();
	}

	private Optional<Job> findByAbbreviation(String abbreviation) {
		return getAll().stream()
			.filter(j -> j.abbreviation().equalsIgnoreCase(abbreviation))
			.findFirst();
	}
}
