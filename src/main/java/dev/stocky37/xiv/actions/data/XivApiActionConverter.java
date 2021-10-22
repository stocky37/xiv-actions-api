package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import dev.stocky37.xiv.actions.xivapi.json.XivApiAction;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class XivApiActionConverter implements Function<XivApiAction, Action> {

	private final int gcdCdGroup;

	@Inject
	public XivApiActionConverter(@ConfigProperty(name = "gcd-cd-group") int gcdCdGroup) {
		this.gcdCdGroup = gcdCdGroup;
	}

	@Override
	public Action apply(XivApiAction action) {
		return new Action(
			String.valueOf(action.ID()),
			action.Name(),
			action.ActionCategory().Name().toLowerCase(),
			action.Description(),
			action.Icon(),
			action.IconHD(),
			action.ActionComboTargetID() == 0
				? Optional.<Integer>empty()
				: Optional.of(action.ActionComboTargetID()),
			Sets.newHashSet(action.CooldownGroup(), action.AdditionalCooldownGroup()),
			action.Recast100ms() * 100,
			action.Cast100ms() * 100,
			action.IsRoleAction() != 0,
			action.ClassJobLevel(),
			gcdCdGroup
		);
	}
}
