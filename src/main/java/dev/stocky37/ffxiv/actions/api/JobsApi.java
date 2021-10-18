package dev.stocky37.ffxiv.actions.api;

import dev.stocky37.ffxiv.actions.core.Job;
import dev.stocky37.ffxiv.actions.core.JobService;
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
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Job getJob(@PathParam int id) {
		return jobs.findById(id);
	}

}
