package dev.stocky37.xiv.actions.core;

import static dev.stocky37.xiv.actions.json.ItemDeserializer.BONUSES;
import static dev.stocky37.xiv.actions.json.ItemDeserializer.BONUS_MAX;
import static org.apache.commons.text.WordUtils.capitalize;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.sort.SortOrder.DESC;

import com.google.common.base.Joiner;
import dev.stocky37.xiv.actions.model.Attribute;
import dev.stocky37.xiv.actions.model.Consumable;
import dev.stocky37.xiv.actions.model.Item;
import dev.stocky37.xiv.actions.model.Job;
import dev.stocky37.xiv.actions.model.Query;
import dev.stocky37.xiv.actions.json.ItemDeserializer;
import dev.stocky37.xiv.actions.xivapi.XivApiClient;
import io.quarkus.cache.CacheResult;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import org.elasticsearch.search.builder.SearchSourceBuilder;

@ApplicationScoped
@SuppressWarnings("UnstableApiUsage")
public class ItemService {
	private static final List<String> INDEXES = List.of("item");
	private static final Joiner joiner = Joiner.on('.');

	private final XivApiClient xivapi;
	private final ItemDeserializer deserializer;

	public ItemService(XivApiClient xivapi, ItemDeserializer deserializer) {
		this.xivapi = xivapi;
		this.deserializer = deserializer;
	}

	@CacheResult(cacheName = "items")
	public List<Consumable> findPotionsForJob(Job job) {
		// skip for jobs without a primary state (dohl etc.)
		if(job.primaryStat().isEmpty()) {
			return Collections.emptyList();
		}

		final Query query = new Query(
			INDEXES,
			ItemDeserializer.ALL_FIELDS,
			buildJobPotionsQuery(job.primaryStat().get())
		);

		return xivapi.search(query, (json) -> (Consumable) deserializer.apply(json));
	}

	@CacheResult(cacheName = "items")
	public Optional<? extends Item> findById(String id) {
		return xivapi.getItem(id);
	}

	private SearchSourceBuilder buildJobPotionsQuery(Attribute attribute) {
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
