package dev.stocky37.xiv.actions.core;

import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.xivapi.XivApi;
import dev.stocky37.xiv.actions.xivapi.json.XivApiClassJob;
import dev.stocky37.xiv.actions.xivapi.json.XivApiListItem;
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
	private final XivApi xivapi;
	private final RateLimiter rateLimiter;
	private final Function<XivApiClassJob, Job> converter;

	@Inject
	public JobService(
		@RestClient XivApi xivapi,
		RateLimiter rateLimiter,
		ActionService actionsService
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.converter = cj -> new Job(
			cj.ID(),
			cj.Name(),
			cj.Abbreviation(),
			cj.Icon(),
			Collections.unmodifiableList(actionsService.findForJob(cj.Abbreviation()))
		);
	}


	@CacheResult(cacheName = "jobs")
	public List<Job> getAll() {
		rateLimiter.acquire();
		return xivapi.classjobs().getAll().Results().parallelStream()
			.filter(x -> x.Name().length() > 0)
			.map(XivApiListItem::ID)
			.map(this::findById)
			.collect(Collectors.toList());
	}

	@CacheResult(cacheName = "jobs")
	public Job findById(int id) {
		rateLimiter.acquire();
		return converter.apply(xivapi.classjobs().getById(id));
	}
}
