package dev.stocky37.xiv.actions.api;

import com.fasterxml.jackson.annotation.JsonView;
import dev.stocky37.xiv.actions.core.JobService;
import dev.stocky37.xiv.actions.data.Job;
import dev.stocky37.xiv.actions.data.Views;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/jobs")
public class JobsApi {

	@Inject JobService jobs;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView(Views.List.class)
	public Multi<Job> listAsync() {
		return jobs.getAllAsync();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<Job> findById(@PathParam("id") int id) {
		return jobs.findByIdAsync(id);
	}

}
