package dev.stocky37.xiv.actions.core;

import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.xivapi.XIVApi;
import dev.stocky37.xiv.actions.xivapi.json.ListItemResource;
import dev.stocky37.xiv.actions.xivapi.json.PaginatedResults;
import dev.stocky37.xiv.actions.xivapi.json.XIVApiClassJob;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class JobService {
	private final XIVApi xivapi;
	private final RateLimiter rateLimiter;
	private final Function<XIVApiClassJob, Job> converter;
	private final String apiKey;

	@Inject
	public JobService(
		@RestClient XIVApi xivapi,
		RateLimiter rateLimiter,
		ActionService actionsService,
		@ConfigProperty(name = "xivapi.api-key") String apiKey
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.apiKey = apiKey;
		this.converter = cj -> new Job(
			cj.ID(),
			cj.Name(),
			cj.Abbreviation(),
			cj.Icon(),
			new ArrayList<>()
//			Collections.unmodifiableList(actionsService.findForJob(cj.Abbreviation()))
		);
	}

	@CacheResult(cacheName = "jobs")
	public Multi<Job> getAllAsync() {
		rateLimiter.acquire();
		return xivapi.classjobs()
			.getAll()
			.map(PaginatedResults::Results)
			.map(results -> results.stream().filter(x -> x.Name().length() > 0).map(ListItemResource::ID))
			.onItem().transformToUni(ids -> {
				//noinspection SimplifyStreamApiCallChains
				List<Uni<? extends Job>> jobs =
					ids.map(this::findByIdAsync).collect(Collectors.toUnmodifiableList());
				return Uni.join()
					.all(jobs)
					.andFailFast();
			}).onItem().transformToMulti(jobs -> Multi.createFrom().iterable(jobs));
	}

	@CacheResult(cacheName = "jobs")
	public Uni<Job> findByIdAsync(int id) {
		rateLimiter.acquire();
		return xivapi.classjobs()
			.findById(id, apiKey)
			.map(converter);
	}
}
