package dev.stocky37.xiv.xivapi;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.model.Item;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.xivapi.json.PaginatedList;
import dev.stocky37.xiv.xivapi.json.SearchBody;
import dev.stocky37.xiv.xivapi.json.XivAbility;
import dev.stocky37.xiv.xivapi.json.XivClassJob;
import dev.stocky37.xiv.xivapi.json.XivStatus;
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
	PaginatedList<XivClassJob> getClassJobs(@QueryParam("columns") List<String> columns);

	@GET
	@Path("action/{id}")
	XivAbility getAction(@PathParam("id") String id);

	@GET
	@Path("item/{id}")
	JsonNode getItem(@PathParam("id") String id);

	@GET
	@Path("status/{id}")
	XivStatus getStatus(@PathParam("id") String id);
}
