package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class XivApiActionConverter implements Function<JsonNode, Action> {

	private final int gcdCdGroup;

	@Inject
	public XivApiActionConverter(@ConfigProperty(name = "gcd-cd-group") int gcdCdGroup) {
		this.gcdCdGroup = gcdCdGroup;
	}

	@Override
	public Action apply(JsonNode action) {
		return new Action(
			action.get("ID").asText(),
			action.get("Name").asText(),
			action.path("ActionCategory").path("Name").asText().toLowerCase(),
			action.get("Description").asText(),
			action.get("Icon").asText(),
			action.get("IconHD").asText(),
			action.get("ActionComboTargetID").asInt() == 0
				? Optional.empty()
				: Optional.of(action.get("ActionComboTargetID").asInt()),
			action.get("CooldownGroup").asInt() == gcdCdGroup,
			action.get("CooldownGroup").asInt(),
			action.get("Recast100ms").asInt() * 100,
			action.get("Cast100ms").asInt() * 100,
			action.get("CastType").asInt(),
			action.get("IsRoleAction").asInt() != 0,
			action.get("ClassJobLevel").asInt()
		);
	}
}
