package dev.stocky37.ffxiv.actions.xivapi;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.ffxiv.actions.xivapi.json.PaginatedResults;
import dev.stocky37.ffxiv.actions.xivapi.json.XIVSearchBody;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://xivapi.com", configKey = "xivapi")
public interface XIVApi {

	@Path("classjob")
	ClassJobsApi classjobs();

	@Path("action")
	ActionsApi actions();

	@POST
	@Path("search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	PaginatedResults<JsonNode> search(XIVSearchBody body);
}
