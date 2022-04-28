package dev.stocky37.xiv.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import java.net.URI;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class Util {

	private static final Slugify slugifier = new Slugify()
		.withCustomReplacement("_", "-")
		.withCustomReplacement("'", "");
	public static final List<String> ALL_COLUMNS = List.of("*");
	// todo: modifiers can be found in the following fields from XIVAPI ClassJob endpoint
	//  - ModifierDexterity
	//  - ModifierHitPoints
	//  - ModifierIntelligence
	//  - ModifierManaPoints
	//  - ModifierMind
	//  - ModifierPiety
	//  - ModifierStrength
	//  - ModifierVitality
	public static final int JOB_MODIFIER = 115;

	private final ObjectMapper json;
	private final UriBuilder prefixBuilder;

	@Inject
	public Util(
		ObjectMapper json,
		@ConfigProperty(name = "xivapi/mp-rest/uri") String baseUri
	) {
		this.json = json;
		this.prefixBuilder = UriBuilder.fromUri(baseUri);
	}

	public static String slugify(String text) {
		return slugifier.slugify(text);
	}

	public JsonNode toJsonNode(Object obj) {
		return json.valueToTree(obj);
	}

	public JsonNode toJsonNode(String obj) {
		try {
			return json.readTree(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T fromJsonNode(JsonNode node, Class<T> clazz) {
		try {
			return json.treeToValue(node, clazz);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public URI prefixXivApi(String relativePath) {
		return prefixBuilder.clone().path(relativePath).build();
	}

	public static int floor(double num) {
		return (int) num;
	}

	public static long floorLong(double num) {
		return (int) num;
	}

	public static double scale(double num) {
		return floor(num) / 1000d;
	}
}
