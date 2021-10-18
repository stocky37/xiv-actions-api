package dev.stocky37.ffxiv.actions.core;

import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.ffxiv.actions.xivapi.XIVApi;
import dev.stocky37.ffxiv.actions.xivapi.json.ListItemResource;
import dev.stocky37.ffxiv.actions.xivapi.json.XIVApiClassJob;
import io.quarkus.cache.CacheResult;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class JobService {
	private final XIVApi xivapi;
	private final RateLimiter rateLimiter;
	private final ActionService actionsService;
	private final Function<XIVApiClassJob, Job> converter;


	@Inject
	public JobService(
		@RestClient XIVApi xivapi,
		RateLimiter rateLimiter,
		ActionService actionsService
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.actionsService = actionsService;
		this.converter = cj -> new Job(cj.ID(),
			cj.Name(),
			cj.Abbreviation(),
			Collections.unmodifiableList(actionsService.findForJob(cj.Abbreviation()))
		);
	}


	@CacheResult(cacheName = "jobs")
	public List<Job> getAll() {
		rateLimiter.acquire();
		return xivapi.classjobs().getAll().Results().stream()
			.filter(x -> x.Name().length() > 0)
			.map(ListItemResource::ID)
			.map(this::findById)
			.collect(Collectors.toList());
	}

	@CacheResult(cacheName = "jobs")
	public Job findById(int id) {
		rateLimiter.acquire();
		return converter.apply(xivapi.classjobs().getById(id));
	}
}
