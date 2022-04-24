package dev.stocky37.xiv.xivapi;

import com.google.common.util.concurrent.RateLimiter;
import dev.stocky37.xiv.model.Query;
import dev.stocky37.xiv.util.Util;
import dev.stocky37.xiv.xivapi.json.SearchBody;
import dev.stocky37.xiv.xivapi.json.XivAbility;
import dev.stocky37.xiv.xivapi.json.XivClassJob;
import dev.stocky37.xiv.xivapi.json.XivConsumable;
import java.util.List;
import java.util.Optional;
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

	public List<XivAbility> searchAbilities(Query query) {
		return search(query, XivAbility.class);
	}

	public List<XivConsumable> searchConsumables(Query query) {
		return search(query, XivConsumable.class);
	}

	public List<XivClassJob> getJobs() {
		return wrapApi(() -> xivapi.getClassJobs(XivClassJob.COLUMNS)).Results();
	}

	public Optional<XivAbility> getAction(String id) {
		try {
			return Optional.of(wrapApi(() -> xivapi.getAction(id)));
		} catch (NotFoundException e) {
			return Optional.empty();
		}
	}

	public Optional<XivConsumable> getConsumable(String id) {
		return getItem(id, XivConsumable.class);
	}

	private <T> List<T> search(Query query, Class<T> clazz) {
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


	private <T> Optional<T> getItem(String id, Class<T> clazz) {
		try {
			return Optional.of(wrapApi(() -> util.fromJsonNode(xivapi.getItem(id), clazz)));
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
