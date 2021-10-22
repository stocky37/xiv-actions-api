package dev.stocky37.xiv.actions.xivapi;

import dev.stocky37.xiv.actions.xivapi.json.ListItemResource;
import dev.stocky37.xiv.actions.xivapi.json.PaginatedResults;
import dev.stocky37.xiv.actions.xivapi.json.XIVApiClassJob;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Produces(MediaType.APPLICATION_JSON)
public interface ClassJobsApi {

	@GET
	PaginatedResults<ListItemResource> getAll();

	@GET
	@Path("{id}")
	XIVApiClassJob getById(@PathParam int id);
}
