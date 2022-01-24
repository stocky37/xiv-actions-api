package dev.stocky37.xiv.actions.core;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.XivApiActionConverter;
import dev.stocky37.xiv.actions.xivapi.XivApi;
import dev.stocky37.xiv.actions.xivapi.json.XivApiAction;
import dev.stocky37.xiv.actions.xivapi.json.XivApiPaginatedList;
import dev.stocky37.xiv.actions.xivapi.json.XivApiSearchBody;
import io.quarkus.cache.CacheResult;
import java.util.List;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class ActionService {
	private static final List<String> SEARCH_COLUMNS = List.of(
		"ID",
		"Name",
		"ActionCategory.Name",
		"Description",
		"Icon",
		"IconHD",
		"ActionComboTargetID",
		"CooldownGroup",
		"AdditionalCooldownGroup",
		"IsRoleAction",
		"Recast100ms",
		"Cast100ms",
		"CastType",
		"ClassJobLevel"
	);
	private static final List<String> INDEXES = List.of("action");
	private final XivApi xivapi;
	private final RateLimiter rateLimiter;
	private final Function<XivApiAction, Action> converter;
	private final ObjectMapper json;

	@Inject
	public ActionService(
		@RestClient XivApi xivapi,
		RateLimiter rateLimiter,
		XivApiActionConverter converter,
		ObjectMapper objectMapper
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.converter = converter;
		this.json = objectMapper;
	}

	public SearchSourceBuilder createActionsQuery(String jobAbbrev) {
		final var queryBody = new SearchSourceBuilder().size(100)
			.sort("IsRoleAction", SortOrder.ASC)
			.sort("ClassJobLevel", SortOrder.ASC);

		final var job = termQuery(String.format("ClassJobCategory.%s", jobAbbrev.toUpperCase()),
			1);
		final var notPvp = termQuery("IsPvP", 0);
		final var hasContentLinks = existsQuery("GameContentLinks");
		final var playerAction = termQuery("IsPlayerAction", 1);
		final var jobLevel = termQuery("ClassJobLevel", 0);

		queryBody.query(boolQuery().filter(job)
			.filter(notPvp)
			.filter(boolQuery().should(hasContentLinks).should(playerAction)).mustNot(jobLevel));

		return queryBody;
	}

	@CacheResult(cacheName = "actions")
	public List<Action> findForJob(String jobAbbreviation) {
		try {
			final JsonNode query = json.readTree(createActionsQuery(jobAbbreviation).toString());
			final XivApiSearchBody body =
				new XivApiSearchBody(String.join(",", INDEXES), String.join(",", SEARCH_COLUMNS), query);
			rateLimiter.acquire();
			final XivApiPaginatedList<JsonNode> results = xivapi.search(body);
			return results.Results().stream().map(node -> {
				try {
					return json.treeToValue(node, XivApiAction.class);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}).map(converter).toList();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}





	}

}
