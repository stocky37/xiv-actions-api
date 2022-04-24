package dev.stocky37.xiv.xivapi.json;

public record XivAbility(
	Long ID,
	String Name,
	String Icon,
	String Description,
	String IconHD,
	Long ActionComboTargetID,
	Integer CooldownGroup,
	Integer AdditionalCooldownGroup,
	Long Cast100ms,
	Long Recast100ms,
	Integer ClassJobLevel,
	Integer AttackTypeTargetID,
	Boolean IsRoleAction,
	ActionCategory ActionCategory
) {
	public record ActionCategory(int ID) {
	}
}
