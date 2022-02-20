package dev.stocky37.xiv.actions.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.stocky37.xiv.actions.data.Ability;
import dev.stocky37.xiv.actions.data.Item;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.json.AbilityDeserializer;
import dev.stocky37.xiv.actions.json.ItemDeserializer;
import dev.stocky37.xiv.actions.json.JobDeserializer;
import io.quarkus.jackson.ObjectMapperCustomizer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class JsonConfiguration implements ObjectMapperCustomizer {

	@Inject AbilityDeserializer abilityDeserializer;
	@Inject ItemDeserializer itemDeserializer;
	@Inject JobDeserializer jobDeserializer;

	public void customize(ObjectMapper mapper) {
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
		mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_ABSENT);
		mapper.registerModule(
			new SimpleModule("deserializers")
				.addDeserializer(Ability.class, abilityDeserializer)
				.addDeserializer(Item.class, itemDeserializer)
				.addDeserializer(Job.class, jobDeserializer)
		);
	}
}
