package dev.stocky37.xiv.actions.json;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.StringJoiner;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JsonNodeWrapper {
	private final JsonNode node;

	public static JsonNodeWrapper from(@Nullable JsonNode node) {
		return node == null ? null : new JsonNodeWrapper(node);
	}

	private JsonNodeWrapper(@Nonnull JsonNode node) {
		this.node = node;
	}

	public JsonNode getNode() {
		return node;
	}

	public JsonNode get(String path) {
		return get(path.split("\\."));
	}

	public JsonNodeWrapper getWrapped(String path) {
		return new JsonNodeWrapper(get(path));
	}

	public JsonNode get(String... paths) {
		final StringJoiner joiner = new StringJoiner("/", "/", "");
		for(final var path : paths) {
			joiner.add(path);
		}
		return node.at(joiner.toString());
	}

	public JsonNodeWrapper getWrapped(String... paths) {
		return new JsonNodeWrapper(get(paths));
	}
}
