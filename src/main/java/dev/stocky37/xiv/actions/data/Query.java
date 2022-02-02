package dev.stocky37.xiv.actions.data;

import java.util.List;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public record Query(
	List<String> indexes,
	List<String> columns,
	SearchSourceBuilder query
) {}
