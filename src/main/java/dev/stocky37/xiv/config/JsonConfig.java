package dev.stocky37.xiv.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.jackson.ObjectMapperCustomizer;
import javax.inject.Singleton;

@Singleton
public final class JsonConfig implements ObjectMapperCustomizer {
	public void customize(ObjectMapper mapper) {
		configure(mapper);
	}

	public static ObjectMapper configure(ObjectMapper mapper) {
		mapper.enable(WRITE_ENUMS_USING_TO_STRING);
		mapper.enable(READ_ENUMS_USING_TO_STRING);
		mapper.disable(WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
		mapper.disable(READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
		mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_ABSENT);
		return mapper;
	}
}
