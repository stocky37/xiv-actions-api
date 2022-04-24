package dev.stocky37.xiv.api;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.annotation.JsonView;
import dev.stocky37.xiv.api.json.RotationInput;
import dev.stocky37.xiv.api.json.Views;
import dev.stocky37.xiv.core.RotationService;
import dev.stocky37.xiv.model.Rotation;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/rotation")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class RotationApi {

	private final RotationService rotations;

	@Inject
	public RotationApi(RotationService rotations) {
		this.rotations = rotations;
	}

	@POST
	@JsonView(Views.Rotation.class)
	public Rotation buildRotation(RotationInput input) {
		return rotations.buildRotation(input);
	}
}

