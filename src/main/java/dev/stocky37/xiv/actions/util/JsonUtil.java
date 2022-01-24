package dev.stocky37.xiv.actions.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JsonUtil {

	private final ObjectMapper mapper;

	@Inject
	public JsonUtil(@SuppressWarnings("CdiInjectionPointsInspection") ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public JsonNode toJsonNode(Object obj) {
		try {
			return mapper.readTree(obj.toString());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T fromJsonNode(JsonNode node, Class<T> valueType) {
		try {
			return mapper.treeToValue(node, valueType);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
