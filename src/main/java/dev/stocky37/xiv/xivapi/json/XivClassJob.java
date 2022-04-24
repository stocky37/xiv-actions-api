package dev.stocky37.xiv.xivapi.json;

public record XivClassJob(
	Long ID,
	String Name,
	String Abbreviation,
	String Icon,
	Integer ClassJobCategoryTargetID,
	Integer Role,
	Integer JobIndex,
	Boolean IsLimitedJob,
	Integer PrimaryStat
) {}
