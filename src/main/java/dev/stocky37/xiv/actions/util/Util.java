package dev.stocky37.xiv.actions.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@SuppressWarnings("CdiInjectionPointsInspection")
public class Util {
	private final ObjectMapper mapper;

	@Inject
	public Util(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public JsonNode toJsonNode(Object obj) {
		try {
			return mapper.readTree(obj.toString());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
