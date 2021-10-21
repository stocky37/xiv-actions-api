package dev.stocky37.ffxiv.actions.data;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;

public record Job(
	@JsonView(Views.ListView.class) int id,
	@JsonView(Views.ListView.class) String name,
	@JsonView(Views.ListView.class) String abbreviation,
	@JsonView(Views.ListView.class) String icon,
	List<Action> actions
) {
}
