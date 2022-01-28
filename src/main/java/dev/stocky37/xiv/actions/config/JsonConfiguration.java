package dev.stocky37.xiv.actions.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.quarkus.jackson.ObjectMapperCustomizer;
import javax.inject.Singleton;

@Singleton
public final class JsonConfiguration implements ObjectMapperCustomizer {

	public void customize(ObjectMapper mapper) {
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_ABSENT);
	}
}
