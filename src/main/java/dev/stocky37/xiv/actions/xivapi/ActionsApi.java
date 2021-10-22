package dev.stocky37.xiv.actions.xivapi;

import dev.stocky37.xiv.actions.xivapi.json.XivApiAction;
import dev.stocky37.xiv.actions.xivapi.json.XivApiListItem;
import dev.stocky37.xiv.actions.xivapi.json.XivApiPaginatedList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Produces(MediaType.APPLICATION_JSON)
public interface ActionsApi {

	@GET
	XivApiPaginatedList<XivApiListItem> getAll();

	@GET
	@Path("{id}")
	XivApiAction getById(@PathParam int id, @QueryParam("private_key") String apikey);
}
