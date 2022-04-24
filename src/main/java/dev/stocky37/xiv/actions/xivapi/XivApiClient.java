package dev.stocky37.xiv.actions.xivapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.actions.model.Item;
import dev.stocky37.xiv.actions.model.Job;
import dev.stocky37.xiv.actions.model.Query;
import dev.stocky37.xiv.actions.json.ItemDeserializer;
import dev.stocky37.xiv.actions.json.JobDeserializer;
import dev.stocky37.xiv.actions.util.Util;
import dev.stocky37.xiv.actions.xivapi.json.SearchBody;
import dev.stocky37.xiv.actions.xivapi.json.XivAbility;
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
	private final Util util;

	@Inject
	public XivApiClient(
		@RestClient XivApi xivapi,
		RateLimiter rateLimiter,
		Util util
	) {
		this.xivapi = xivapi;
		this.rateLimiter = rateLimiter;
		this.util = util;
	}

	public <T> List<T> search(Query query, Function<JsonNode, T> deserializer) {
		final SearchBody body = new SearchBody(
			String.join(",", query.indexes()),
			String.join(",", query.columns()),
			util.toJsonNode(query.query())
		);

		return wrapApi(() -> xivapi.search(body))
			.Results()
			.parallelStream()
			.map(deserializer)
			.toList();
	}

	public <T> List<T> search2(Query query, Class<T> clazz) {
		final SearchBody body = new SearchBody(
			String.join(",", query.indexes()),
			String.join(",", query.columns()),
			util.toJsonNode(query.query())
		);

		return wrapApi(() -> xivapi.search(body))
			.Results()
			.parallelStream()
			.map((node) -> util.fromJsonNode(node, clazz))
			.toList();
	}

	public List<XivAbility> searchAbilities(Query query) {
		return search2(query, XivAbility.class);
	}

	public List<Job> getJobs() {
		return wrapApi(() -> xivapi.getClassJobs(JobDeserializer.ALL_FIELDS)).Results();
	}

	public Optional<XivAbility> getAction(String id) {
		try {
			return Optional.of(wrapApi(() -> xivapi.getAction(id)));
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
}
