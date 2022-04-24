package dev.stocky37.xiv.actions.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Util {

	private final ObjectMapper mapper;

	@Inject
	public Util(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	private static final Slugify slugifier = new Slugify().withCustomReplacement("_", "-");

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
}
