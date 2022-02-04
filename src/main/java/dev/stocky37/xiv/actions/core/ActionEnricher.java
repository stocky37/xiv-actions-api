package dev.stocky37.xiv.actions.core;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.json.ActionDeserializer;
import dev.stocky37.xiv.actions.json.JsonNodeWrapper;
import java.time.Duration;
import java.util.Map;
import java.util.function.UnaryOperator;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SuppressWarnings("UnstableApiUsage")
@ApplicationScoped
public class ActionEnricher implements UnaryOperator<Action> {
	private final Map<String, JsonNode> data;

	@Inject
	public ActionEnricher(@Named("data.actions") Map<String, JsonNode> data) {
		this.data = data;
	}

	@Override
	public Action apply(Action action) {
		final JsonNodeWrapper json = JsonNodeWrapper.from(data.get(action.id()));
		if(json == null) {
			return action;
		}
		final Action.Builder builder = Action.builder(action);
		if(!json.get(ActionDeserializer.RECAST).isMissingNode()) {
			builder.withRecast(Duration.ofMillis(json.get(ActionDeserializer.RECAST).asLong() * 100));
		}
		return builder.build();
	}
}
