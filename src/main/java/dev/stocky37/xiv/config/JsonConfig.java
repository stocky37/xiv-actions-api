package dev.stocky37.xiv.config;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.stocky37.xiv.json.ItemDeserializer;
import dev.stocky37.xiv.json.JobDeserializer;
import dev.stocky37.xiv.model.Item;
import dev.stocky37.xiv.model.Job;
import io.quarkus.jackson.ObjectMapperCustomizer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class JsonConfig implements ObjectMapperCustomizer {

	@Inject ItemDeserializer itemDeserializer;
	@Inject JobDeserializer jobDeserializer;

	public void customize(ObjectMapper mapper) {
		mapper.enable(WRITE_ENUMS_USING_TO_STRING);
		mapper.disable(WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
		mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_ABSENT);
		mapper.registerModule(
			new SimpleModule("deserializers")
				.addDeserializer(Item.class, itemDeserializer)
				.addDeserializer(Job.class, jobDeserializer)
		);
	}
}
