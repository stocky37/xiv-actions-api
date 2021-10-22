package dev.stocky37.xiv.actions.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.jackson.ObjectMapperCustomizer;
import javax.inject.Singleton;

@Singleton
public final class JsonConfiguration implements ObjectMapperCustomizer {

	public void customize(ObjectMapper mapper) {
		mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
	}
}
