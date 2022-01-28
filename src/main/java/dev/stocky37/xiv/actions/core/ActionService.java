package dev.stocky37.xiv.actions.core;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.ActionConverter;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.util.Util;
import dev.stocky37.xiv.actions.xivapi.XivApi;
import dev.stocky37.xiv.actions.xivapi.json.PaginatedList;
import dev.stocky37.xiv.actions.xivapi.json.SearchBody;
import io.quarkus.cache.CacheResult;
import java.util.List;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class ActionService {
	private static final List<String> INDEXES = List.of("action");

	private final XivApi xivapi;
	private final RateLimiter rateLimiter;
	private final Function<JsonNode, Action> converter;
	private final Util json;

	@Inject
	public ActionService(
		@RestClient XivApi xivapi,
		RateLimiter rateLimiter,
		ActionConverter converter,
		Util json
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.converter = converter;
		this.json = json;
	}

	@CacheResult(cacheName = "actions")
	public List<Action> findForJob(Job job) {
		final JsonNode query = json.toJsonNode(createActionsQuery(job.abbreviation()));
		final SearchBody body = new SearchBody(
			String.join(",", INDEXES),
			String.join(",", ActionConverter.ALL_FIELDS),
			query
		);
		rateLimiter.acquire();
		final PaginatedList<JsonNode> results = xivapi.search(body);
		return results.Results().parallelStream().map(converter).toList();
	}

	private SearchSourceBuilder createActionsQuery(String jobAbbrev) {
		final var job = termQuery(
			String.format("ClassJobCategory.%s", jobAbbrev.toUpperCase()),
			1
		);
		final var notPvp = termQuery("IsPvP", 0);
		final var hasContentLinks = existsQuery("GameContentLinks");
		final var playerAction = termQuery("IsPlayerAction", 1);
		final var jobLevel = termQuery("ClassJobLevel", 0);

		return new SearchSourceBuilder().size(100)
			.sort("IsRoleAction", SortOrder.ASC)
			.sort("ClassJobLevel", SortOrder.ASC)
			.query(boolQuery()
				.filter(job)
				.filter(notPvp)
				.filter(boolQuery()
					.should(hasContentLinks)
					.should(playerAction)
				)
				.mustNot(jobLevel)
			);
	}

}
