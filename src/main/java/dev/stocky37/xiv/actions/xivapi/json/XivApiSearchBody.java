package dev.stocky37.xiv.actions.xivapi.json;

import com.fasterxml.jackson.databind.JsonNode;

public record XivApiSearchBody(
	String indexes,
	String columns,
	JsonNode body
) {
}
