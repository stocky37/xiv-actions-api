package dev.stocky37.xiv.actions.util;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.StringJoiner;
import javax.ws.rs.core.UriBuilder;

public class JsonNodeWrapper {

	private final JsonNode node;
	private final UriBuilder uriBuilder;

	public JsonNodeWrapper(JsonNode node, String uriBuilder) {
		this.node = node;
		this.uriBuilder = UriBuilder.fromUri(uriBuilder);
	}

	public JsonNode node() {
		return node;
	}

	public String getText(String path) {
		return get(path).asText();
	}

	public int getInt(String path) {
		return get(path).asInt();
	}

	public long getLong(String path) {
		return get(path).asLong();
	}

	public boolean getBool(String path) {
		return get(path).asBoolean();
	}

	public URI getUri(String path) {
		return prefixUri(getText(path));
	}

	public JsonNode get(String... paths) {
		final StringJoiner joiner = new StringJoiner("/", "/", "");
		for(final var path : paths) {
			joiner.add(path);
		}
		return node.at(joiner.toString());
	}

	public JsonNode get(String path) {
		return get(path.split("\\."));
	}

	private URI prefixUri(String relativePath) {
		return uriBuilder.clone().path(relativePath).build();
	}
}
