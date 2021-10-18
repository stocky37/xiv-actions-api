package dev.stocky37.ffxiv.actions.xivapi.json;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record XIVApiAction(
	int ID,
	String Name,
	ActionCategory ActionCategory,
	String Description,
	String Icon,
	String IconHD,
	int ActionComboTargetID,
	int CooldownGroup,
	int IsRoleAction,
	int Recast100ms,
	int Cast100ms,
	int CastType,
	int ClassJobLevel
) {

	public static record ActionCategory(String Name) {

	}
}

