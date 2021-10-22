package dev.stocky37.xiv.actions.xivapi.json;

public record XivApiClassJob(
	int ID,
	String Name,
	String Abbreviation,
	String Icon,
	int Role,
	int PrimaryStat,
	int ClassJobParentTargetID
) {
}