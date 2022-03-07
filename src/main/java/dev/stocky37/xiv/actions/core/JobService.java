package dev.stocky37.xiv.actions.core;

import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.xivapi.XivApiClient;
import io.quarkus.cache.CacheResult;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@SuppressWarnings("UnstableApiUsage")
public class JobService {
	private final XivApiClient xivapi;
	private final UnaryOperator<Job> enricher;

	public JobService(XivApiClient xivapi, UnaryOperator<Job> enricher) {
		this.xivapi = xivapi;
		this.enricher = enricher;
	}

	public List<Job> getAll() {
		return getAll(Optional.empty());
	}

	@CacheResult(cacheName = "jobs")
	public List<Job> getAll(Optional<Job.Type> type) {
		final var jobs = xivapi.getJobs();
		return type.isPresent() ? jobs.stream().filter(j -> j.type() == type.get()).toList() : jobs;
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
