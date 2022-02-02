package dev.stocky37.xiv.actions.core;

import static dev.stocky37.xiv.actions.data.ItemConverter.BONUSES;
import static dev.stocky37.xiv.actions.data.ItemConverter.BONUS_MAX;
import static org.elasticsearch.common.Strings.capitalize;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.sort.SortOrder.DESC;

import com.google.common.base.Joiner;
import dev.stocky37.xiv.actions.data.Attribute;
import dev.stocky37.xiv.actions.data.Item;
import dev.stocky37.xiv.actions.data.ItemConverter;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.data.Query;
import dev.stocky37.xiv.actions.xivapi.XivApiClient;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.elasticsearch.search.builder.SearchSourceBuilder;

@ApplicationScoped
@SuppressWarnings("UnstableApiUsage")
public class ItemService {
	private static final List<String> INDEXES = List.of("item");
	private static final Joiner joiner = Joiner.on('.');

	private final XivApiClient xivapi;
	private final ItemConverter converter;

	public ItemService(XivApiClient xivapi, ItemConverter converter) {
		this.xivapi = xivapi;
		this.converter = converter;
	}

	public List<Item> findPotionsForJob(Job job) {
		// skip for jobs without a primary state (dohl etc.)
		if(job.primaryStat().isEmpty()) {
			return Collections.emptyList();
		}

		final Query query = new Query(
			INDEXES,
			ItemConverter.ALL_FIELDS,
			buildJobPotionsQuery(job.primaryStat().get())
		);

		return xivapi.search(query, converter);
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
