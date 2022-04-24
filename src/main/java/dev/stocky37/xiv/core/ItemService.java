package dev.stocky37.xiv.core;

import static org.apache.commons.text.WordUtils.capitalize;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.sort.SortOrder.DESC;

import com.google.common.base.Joiner;
import dev.stocky37.xiv.model.Attribute;
import dev.stocky37.xiv.model.Consumable;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.Query;
import dev.stocky37.xiv.model.transform.ConsumableConverter;
import dev.stocky37.xiv.util.Util;
import dev.stocky37.xiv.xivapi.XivApiClient;
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
	private final ConsumableConverter converter;

	public ItemService(XivApiClient xivapi, ConsumableConverter converter) {
		this.xivapi = xivapi;
		this.converter = converter;
	}

	@CacheResult(cacheName = "items")
	public List<Consumable> findPotionsForJob(Job job) {
		// skip for jobs without a primary state (dohl etc.)
		if(job.primaryStat().isEmpty()) {
			return Collections.emptyList();
		}

		final Query query = new Query(
			INDEXES,
			Util.ALL_COLUMNS,
			buildJobPotionsQuery(job.primaryStat().get())
		);

		return xivapi.searchConsumables(query).stream().map(converter).toList();
	}

	@CacheResult(cacheName = "items")
	public Optional<Consumable> findConsumableById(String id) {
		return xivapi.getConsumable(id).map(converter);
	}

	private SearchSourceBuilder buildJobPotionsQuery(Attribute attribute) {
		final var isPotion = termQuery("ItemSortCategory.ID", 6);
		final var hasStat = existsQuery(joiner.join("Bonuses", capitalize(attribute.toString())));

		return new SearchSourceBuilder().size(100)
			.sort(
				Joiner.on('.').join("Bonuses", capitalize(attribute.toString()), "MaxHQ"),
				DESC
			)
			.query(boolQuery()
				.filter(isPotion)
				.filter(hasStat)
			);
	}
}
