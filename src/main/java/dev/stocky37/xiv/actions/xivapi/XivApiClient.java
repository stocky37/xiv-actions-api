package dev.stocky37.xiv.actions.xivapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.actions.data.Ability;
import dev.stocky37.xiv.actions.data.Item;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.data.Query;
import dev.stocky37.xiv.actions.json.AbilityDeserializer;
import dev.stocky37.xiv.actions.json.ItemDeserializer;
import dev.stocky37.xiv.actions.json.JobDeserializer;
import dev.stocky37.xiv.actions.xivapi.json.SearchBody;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class XivApiClient {
	private final XivApi xivapi;
	private final RateLimiter rateLimiter;
	private final ObjectMapper mapper;

	@Inject
	public XivApiClient(
		@RestClient XivApi xivapi,
		RateLimiter rateLimiter,
		ObjectMapper mapper
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.mapper = mapper;
	}

	public <T> List<T> search(Query query, Function<JsonNode, T> deserializer) {
		final SearchBody body = new SearchBody(
			String.join(",", query.indexes()),
			String.join(",", query.columns()),
			toJsonNode(query.query())
		);

		return wrapApi(() -> xivapi.search(body))
			.Results()
			.parallelStream()
			.map(deserializer)
			.toList();
	}

	public List<Job> getJobs() {
		return wrapApi(() -> xivapi.getClassJobs(JobDeserializer.ALL_FIELDS)).Results();
	}

	public Optional<Ability> getAction(String id) {
		try {
			return Optional.of(wrapApi(() -> xivapi.getAction(id, AbilityDeserializer.ALL_FIELDS)));
		} catch (NotFoundException e) {
			return Optional.empty();
		}
	}

	public Optional<Item> getItem(String id) {
		try {
			return Optional.of(wrapApi(() -> xivapi.getItem(id, ItemDeserializer.ALL_FIELDS)));
		} catch (NotFoundException e) {
			return Optional.empty();
		}
	}

	private <T> T wrapApi(Supplier<T> supplier) {
		rateLimiter.acquire();
		try {
			return supplier.get();
		} catch (WebApplicationException e) {
			if(e.getResponse().getStatusInfo() == Response.Status.NOT_FOUND) {
				throw new NotFoundException(e);
			} else {
				throw new InternalServerErrorException(e);
			}
		}
	}

	private JsonNode toJsonNode(Object obj) {
		try {
			return mapper.readTree(obj.toString());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
