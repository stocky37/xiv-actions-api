package dev.stocky37.xiv.actions.core;

import static java.lang.Integer.parseInt;

import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.data.XivApiJobConverter;
import dev.stocky37.xiv.actions.util.JsonUtil;
import dev.stocky37.xiv.actions.xivapi.XivApi;
import dev.stocky37.xiv.actions.xivapi.json.XivApiClassJob;
import io.quarkus.cache.CacheResult;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class JobService {
	private static final List<String> COLUMNS = List.of(
		"ID",
		"Name",
		"Abbreviation",
		"Icon",
		"ClassJobCategoryTargetID",
		"JobIndex",
		"Role",
		"IsLimitedJob"
	);

	private final XivApi xivapi;
	private final RateLimiter rateLimiter;
	private final Function<XivApiClassJob, Job> unenrichedConverter;
	private final Function<XivApiClassJob, Job> enrichedConverter;
	private final JsonUtil json;

	@Inject
	public JobService(
		@RestClient XivApi xivapi,
		RateLimiter rateLimiter,
		@Named("enriched") XivApiJobConverter enrichedConverter,
		@Named("unenriched") XivApiJobConverter unenrichedConverter,
		JsonUtil json
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.enrichedConverter = enrichedConverter;
		this.unenrichedConverter = unenrichedConverter;
		this.json = json;
	}

	@CacheResult(cacheName = "jobs")
	public List<Job> getAll() {
		rateLimiter.acquire();
		return xivapi.classjobs().getAll(COLUMNS).Results().parallelStream()
			.map(node -> json.fromJsonNode(node, XivApiClassJob.class))
			.filter(x -> x.Name().length() > 0)
			.map(unenrichedConverter)
			.collect(Collectors.toList());
	}

	@CacheResult(cacheName = "jobs")
	public Optional<Job> findByIdentifier(String identifier) {
		return findById(identifier)
			.or(() -> findByName(identifier))
			.or(() -> findByAbbreviation(identifier));
	}

	@CacheResult(cacheName = "jobsById")
	public Optional<Job> findById(String id) {
		rateLimiter.acquire();
		try {
			return Optional.ofNullable(xivapi.classjobs().getById(parseInt(id))).map(enrichedConverter);
		} catch (NumberFormatException nfe) {
			System.err.println("return empty");
			return Optional.empty();
		}
	}


	// can ignore because if the job exists, the id will exist
	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public Optional<Job> findByName(String name) {
		System.err.println("name: " + name);
		return getAll().stream()
			.filter(j -> j.name().equalsIgnoreCase(name))
			.findFirst()
			.map(job -> findById(job.id()).get());
	}

	// can ignore because if the job exists, the id will exist
	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public Optional<Job> findByAbbreviation(String abbreviation) {
		System.err.println("findByName");
		return getAll().stream()
			.filter(j -> j.abbreviation().equalsIgnoreCase(abbreviation))
			.findFirst()
			.map(job -> findById(job.id()).get());
	}
}
