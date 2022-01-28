package dev.stocky37.xiv.actions.xivapi;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.xivapi.json.PaginatedList;
import dev.stocky37.xiv.actions.xivapi.json.SearchBody;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "xivapi")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface XivApi {

	@POST
	@Path("search")
	PaginatedList<JsonNode> search(SearchBody body);

	@GET
	@Path("classjob")
	PaginatedList<JsonNode> getClassJobs(@QueryParam("columns") List<String> columns);
}
