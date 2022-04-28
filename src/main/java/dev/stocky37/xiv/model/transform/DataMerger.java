package dev.stocky37.xiv.model.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import dev.stocky37.xiv.util.Util;
import java.util.function.BiFunction;

public class DataMerger<T> implements BiFunction<T, JsonNode, T> {

	private final Util util;
	private final Class<T> clazz;

	public DataMerger(Util util, Class<T> clazz) {
		this.util = util;
		this.clazz = clazz;
	}

	@Override
	public T apply(T source, JsonNode update) {
		try {
			final var patch = JsonMergePatch.fromJson(update);
			return util.fromJsonNode(patch.apply(util.toJsonNode(source)), clazz);
		} catch (JsonPatchException e) {
			throw new RuntimeException(e);
		}
	}
}
