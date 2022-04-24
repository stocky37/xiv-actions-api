package dev.stocky37.xiv.xivapi.json;

import java.util.List;

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
) {

	public static final List<String> COLUMNS = List.of(
		"ID", "Name", "Abbreviation", "Icon",
		"ClassJobCategoryTargetID", "Role", "JobIndex", "IsLimitedJob", "PrimaryStat"
	);
}
