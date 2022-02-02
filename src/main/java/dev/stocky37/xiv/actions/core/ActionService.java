package dev.stocky37.xiv.actions.core;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.ActionConverter;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.data.Query;
import dev.stocky37.xiv.actions.xivapi.XivApiClient;
import io.quarkus.cache.CacheResult;
import java.util.List;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class ActionService {
	private static final List<String> INDEXES = List.of("action");

	private final XivApiClient xivapi;
	private final Function<JsonNode, Action> converter;

	public ActionService(XivApiClient xivapi, ActionConverter converter) {
		this.xivapi = xivapi;
		this.converter = converter;
	}

	@CacheResult(cacheName = "actions")
	public List<Action> findForJob(Job job) {
		final Query query = new Query(
			INDEXES,
			ActionConverter.ALL_FIELDS,
			buildJobActionsQuery(job.abbreviation())
		);

		return xivapi.search(query, converter);
	}

	private SearchSourceBuilder buildJobActionsQuery(String jobAbbrev) {
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
