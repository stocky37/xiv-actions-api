package dev.stocky37.xiv.actions.xivapi;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.Item;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.xivapi.json.PaginatedList;
import dev.stocky37.xiv.actions.xivapi.json.SearchBody;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
	PaginatedList<Job> getClassJobs(@QueryParam("columns") List<String> columns);

	@GET
	@Path("action/{id}")
	Action getAction(@PathParam("id") String id, @QueryParam("columns") List<String> columns);

	@GET
	@Path("item/{id}")
	Item getItem(@PathParam("id") String id, @QueryParam("columns") List<String> columns);

//	@GET
//	@Path("status/{id}")
//	Item getItem(@PathParam("id") String id, @QueryParam("columns") List<String> columns);
}
