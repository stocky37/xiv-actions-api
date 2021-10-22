package dev.stocky37.xiv.actions.xivapi;

import dev.stocky37.xiv.actions.xivapi.json.ListItemResource;
import dev.stocky37.xiv.actions.xivapi.json.PaginatedResults;
import dev.stocky37.xiv.actions.xivapi.json.XIVApiClassJob;
import io.smallrye.mutiny.Uni;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public interface ClassJobsApi {
	@GET
	Uni<PaginatedResults<ListItemResource>> getAll();

	@GET
	@Path("{id}")
	Uni<XIVApiClassJob> findById(
		@PathParam("id") int id,
		@QueryParam("private_key") String apiKey
	);
}
