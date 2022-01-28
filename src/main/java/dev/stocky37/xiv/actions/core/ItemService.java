package dev.stocky37.xiv.actions.core;

import static dev.stocky37.xiv.actions.data.ItemConverter.BONUSES;
import static dev.stocky37.xiv.actions.data.ItemConverter.BONUS_MAX;
import static org.elasticsearch.common.Strings.capitalize;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.sort.SortOrder.DESC;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.actions.data.Attribute;
import dev.stocky37.xiv.actions.data.Item;
import dev.stocky37.xiv.actions.data.ItemConverter;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.util.JsonUtil;
import dev.stocky37.xiv.actions.xivapi.XivApi;
import dev.stocky37.xiv.actions.xivapi.json.PaginatedList;
import dev.stocky37.xiv.actions.xivapi.json.SearchBody;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;

@ApplicationScoped
@SuppressWarnings("UnstableApiUsage")
public class ItemService {
	private static final List<String> INDEXES = List.of("item");
	private static final Joiner joiner = Joiner.on('.');

	private final XivApi xivapi;
	private final RateLimiter rateLimiter;
	private final ItemConverter converter;
	private final JsonUtil json;

	@Inject
	public ItemService(
		@RestClient XivApi xivapi,
		RateLimiter rateLimiter,
		ItemConverter converter,
		JsonUtil json
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.converter = converter;
		this.json = json;
	}

	public List<Item> findPotionsForJob(Job job) {
		if(job.primaryStat().isEmpty()) {
			return Collections.emptyList();
		}

		final JsonNode query = json.toJsonNode(createPotionsQuery(job.primaryStat().get()));
		final SearchBody body = new SearchBody(
			String.join(",", INDEXES),
			String.join(",", ItemConverter.ALL_FIELDS),
			query
		);
		rateLimiter.acquire();
		final PaginatedList<JsonNode> results = xivapi.search(body);
		return results.Results().parallelStream().map(converter).toList();
	}

	private SearchSourceBuilder createPotionsQuery(Attribute attribute) {
		final var isPotion = termQuery("ItemSortCategory.ID", 6);
		final var hasStat = existsQuery(joiner.join(BONUSES, capitalize(attribute.toString())));

		return new SearchSourceBuilder().size(100)
			.sort(
				Joiner.on('.').join(BONUSES, capitalize(attribute.toString()), BONUS_MAX),
				DESC
			)
			.query(boolQuery()
				.filter(isPotion)
				.filter(hasStat)
			);
	}
}
