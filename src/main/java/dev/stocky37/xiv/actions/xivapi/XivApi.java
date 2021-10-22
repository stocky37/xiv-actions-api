package dev.stocky37.xiv.actions.xivapi;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.xivapi.json.XivApiPaginatedList;
import dev.stocky37.xiv.actions.xivapi.json.XivApiSearchBody;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://xivapi.com", configKey = "xivapi")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface XivApi {

	@Path("classjob")
	ClassJobsApi classjobs();

	@Path("action")
	ActionsApi actions();

	@POST
	@Path("search")
	XivApiPaginatedList<JsonNode> search(XivApiSearchBody body);
}
