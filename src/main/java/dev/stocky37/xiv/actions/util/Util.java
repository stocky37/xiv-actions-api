package dev.stocky37.xiv.actions.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class Util {
	private final ObjectMapper mapper;
	private final String xivapiUri;

	@Inject
	public Util(
		@SuppressWarnings("CdiInjectionPointsInspection") ObjectMapper mapper,
		@ConfigProperty(name = "xivapi/mp-rest/uri") String xivapiUri
	) {
		this.mapper = mapper;
		this.xivapiUri = xivapiUri;
	}

	public JsonNode toJsonNode(Object obj) {
		try {
			return mapper.readTree(obj.toString());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public JsonNodeWrapper wrapNode(JsonNode node) {
		return new JsonNodeWrapper(node, xivapiUri);
	}
}
