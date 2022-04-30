package dev.stocky37.xiv.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.io.Resources;
import io.quarkus.logging.Log;
import java.io.IOException;
import javax.inject.Named;
import javax.inject.Singleton;

public class DataLoader {

	private final static YAMLMapper yaml = (YAMLMapper) JsonConfig.configure(YAMLMapper.builder().build());

	@Singleton
	@Named("abilities.data")
	public JsonNode loadActionData() {
		return loadData("data/abilities.yml");
	}

	@Singleton
	@Named("statuses.data")
	public JsonNode loadStatusData() {
		return loadData("data/statuses.yml");
	}

	@Singleton
	@Named("jobs.data")
	public JsonNode loadJobsData() {
		return loadData("data/jobs.yml");
	}

	public static JsonNode loadData(String resource) {
		try {
			//noinspection UnstableApiUsage
			return yaml.readTree(Resources.getResource(resource));
		} catch (IllegalArgumentException | IOException e) {
			Log.warn("Failed to load data", e);
			return NullNode.getInstance();
		}
	}
}
