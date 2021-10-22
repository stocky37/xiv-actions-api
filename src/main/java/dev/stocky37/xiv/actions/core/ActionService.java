package dev.stocky37.xiv.actions.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.XivApiActionConverter;
import dev.stocky37.xiv.actions.xivapi.XivApi;
import dev.stocky37.xiv.actions.xivapi.json.XivApiPaginatedList;
import dev.stocky37.xiv.actions.xivapi.json.XivApiSearchBody;
import io.quarkus.cache.CacheResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

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
		"IsRoleAction",
		"Recast100ms",
		"Cast100ms",
		"CastType",
		"ClassJobLevel"
	);
	private static final List<String> INDEXES = List.of("action");
	private final XivApi xivapi;
	private final RateLimiter rateLimiter;
	private final Function<JsonNode, Action> converter;

	@Inject
	public ActionService(
		@RestClient XivApi xivapi,
		RateLimiter rateLimiter,
		XivApiActionConverter converter
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.converter = converter;
	}

	public static Map<String, Object> createActionsQuery(String jobAbbrev) {
		final Map<String, Object> query = new HashMap<>();
		final Map<String, Object> mustNot = Map.of("term", Map.of("ClassJobLevel", 0));
		final List<Map<String, Object>> filters = List.of(
			Map.of("term", Map.of("IsPvP", 0)),
			Map.of("term", Map.of(String.format("ClassJobCategory.%s", jobAbbrev.toUpperCase()), 1))
		);
		query.put("query", Map.of("bool", Map.of("must_not", mustNot, "filter", filters)));
		query.put("from", 0);
		query.put("size", 100);
		query.put("sort", List.of(
			Map.of("IsRoleAction", "asc"),
			Map.of("ClassJobLevel", "asc")
		));
		return query;
	}

	@CacheResult(cacheName = "actions")
	public List<Action> findForJob(String jobAbbreviation) {
		final Map<String, Object> obj = createActionsQuery(jobAbbreviation);
		final XivApiSearchBody body =
			new XivApiSearchBody(String.join(",", INDEXES), String.join(",", SEARCH_COLUMNS), obj);

		rateLimiter.acquire();
		final XivApiPaginatedList<JsonNode> results = xivapi.search(body);
		return results.Results().stream().map(converter).toList();
	}

}
