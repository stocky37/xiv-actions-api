package dev.stocky37.xiv.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.net.URI;
import java.util.function.Function;
import javax.ws.rs.core.UriBuilder;

public abstract class JsonNodeDeserializer<T> extends StdDeserializer<T>
	implements Function<JsonNode, T> {

	private final UriBuilder uriBuilder;

	protected JsonNodeDeserializer(Class<?> vc, String baseUri) {
		super(vc);
		this.uriBuilder = UriBuilder.fromUri(baseUri);
	}

	@Override
	public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		return apply((JsonNode) p.getCodec().readTree(p));
	}

	@Override
	public T apply(JsonNode json) {
		return apply(JsonNodeWrapper.from(json));
	}

	public abstract T apply(JsonNodeWrapper json);

	protected URI getUri(JsonNodeWrapper json, String path) {
		return prefixUri(json.get(path).asText());
	}

	protected URI prefixUri(String relativePath) {
		return uriBuilder.clone().path(relativePath).build();
	}
}
