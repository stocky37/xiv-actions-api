package dev.stocky37.xiv.actions.xivapi;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.xivapi.json.XivApiClassJob;
import dev.stocky37.xiv.actions.xivapi.json.XivApiPaginatedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public interface ClassJobsApi {

	@GET
	XivApiPaginatedList<JsonNode> getAll(@QueryParam("columns") List<String> columns);

	@GET
	@Path("{id}")
	XivApiClassJob getById(@PathParam("id") int id);
}
