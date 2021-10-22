package dev.stocky37.xiv.actions.api;

import com.fasterxml.jackson.annotation.JsonView;
import dev.stocky37.xiv.actions.core.JobService;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.data.Views;
import java.util.Collection;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/jobs")
public class JobsApi {

	@Inject JobService jobs;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView(Views.List.class)
	public Collection<Job> list() {
		return jobs.getAll();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Job findById(@PathParam int id) {
		return jobs.findById(id);
	}

}
