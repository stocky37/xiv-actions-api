package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;

public record Job(
	@JsonView(Views.List.class) String id,
	@JsonView(Views.List.class) String name,
	@JsonView(Views.List.class) String abbreviation,
	@JsonView(Views.List.class) String icon,
	List<Action> actions
) {
	
}