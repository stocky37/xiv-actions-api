package dev.stocky37.xiv.actions.core;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.data.Ability;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.data.Query;
import dev.stocky37.xiv.actions.json.AbilityDeserializer;
import dev.stocky37.xiv.actions.xivapi.XivApiClient;
import io.quarkus.cache.CacheResult;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import javax.enterprise.context.ApplicationScoped;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class AbilityService {
	private static final List<String> INDEXES = List.of("action");

	private final XivApiClient xivapi;
	private final Function<JsonNode, Ability> converter;
	private final UnaryOperator<Ability> enricher;

	public AbilityService(
		XivApiClient xivapi, AbilityDeserializer converter,
		AbilityEnricher enricher
	) {
		this.xivapi = xivapi;
		this.converter = converter;
		this.enricher = enricher;
	}

	@CacheResult(cacheName = "actions")
	public List<Ability> findForJob(Job job) {
		final Query query = new Query(
			INDEXES,
			AbilityDeserializer.ALL_FIELDS,
			buildJobActionsQuery(job.abbreviation())
		);

		return xivapi.search(query, converter).stream().map(enricher).toList();
	}

	@CacheResult(cacheName = "actions")
	public Optional<Ability> findById(String id) {
		return xivapi.getAction(id).map(enricher);
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
