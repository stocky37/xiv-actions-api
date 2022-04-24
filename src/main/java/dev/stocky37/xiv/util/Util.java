package dev.stocky37.xiv.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import java.net.URI;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Util {

	private static final Slugify slugifier = new Slugify().withCustomReplacement("_", "-");

	private final ObjectMapper mapper;
	private final UriBuilder prefixBuilder;

	@Inject
	public Util(
		ObjectMapper mapper,
		@ConfigProperty(name = "xivapi/mp-rest/uri") String baseUri
	) {
		this.mapper = mapper;
		this.prefixBuilder = UriBuilder.fromUri(baseUri);
	}

	public static String slugify(String text) {
		return slugifier.slugify(text);
	}

	public JsonNode toJsonNode(Object obj) {
		try {
			return mapper.readTree(obj.toString());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T fromJsonNode(JsonNode node, Class<T> clazz) {
		try {
			return mapper.treeToValue(node, clazz);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public URI prefixXivApi(String relativePath) {
		return prefixBuilder.clone().path(relativePath).build();
	}
}
