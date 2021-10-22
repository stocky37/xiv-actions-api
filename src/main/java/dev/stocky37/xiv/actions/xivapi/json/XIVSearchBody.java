package dev.stocky37.xiv.actions.xivapi.json;

import java.util.Map;

public record XIVSearchBody(
	String indexes,
	String columns,
	Map<String, Object> body
) {
}
