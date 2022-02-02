package dev.stocky37.xiv.actions.json;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.actions.data.Attribute;
import dev.stocky37.xiv.actions.data.Job;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class JobDeserializer extends JsonNodeDeserializer<Job> {

	public static final String ID = "ID";
	public static final String NAME = "Name";
	public static final String ABBREV = "Abbreviation";
	public static final String ICON = "Icon";
	public static final String CATEGORY = "ClassJobCategoryTargetID";
	public static final String ROLE = "Role";
	public static final String JOB_INDEX = "JobIndex";
	public static final String IS_LIMITED = "IsLimitedJob";
	public static final String PRIMARY_STAT = "PrimaryStat";

	public static final List<String> ALL_FIELDS = List.of(
		ID, NAME, ABBREV, ICON, CATEGORY, ROLE, JOB_INDEX, IS_LIMITED, PRIMARY_STAT
	);

	@Inject
	public JobDeserializer(@ConfigProperty(name = "xivapi/mp-rest/uri") String baseUri) {
		super(Job.class, baseUri);
	}

	@Override
	public Job apply(JsonNode node) {
		return new Job(
			get(node, ID).asText(),
			get(node, NAME).asText(),
			get(node, ABBREV).asText(),
			getUri(node, ICON),
			category(get(node, CATEGORY).asInt()),
			type(get(node, JOB_INDEX).asInt()),
			role(get(node, ROLE).asInt()),
			get(node, JOB_INDEX).asInt(),
			get(node, IS_LIMITED).asBoolean(),
			primaryStat(get(node, PRIMARY_STAT).asInt())
		);
	}

	private Job.Category category(int categoryId) {
		return switch(categoryId) {
			case 30 -> Job.Category.DOW;
			case 31 -> Job.Category.DOM;
			case 32 -> Job.Category.DOL;
			case 33 -> Job.Category.DOH;
			default -> throw new RuntimeException("Unknown category: " + categoryId);
		};
	}

	private Job.Type type(int jobIndex) {
		return jobIndex > 0 ? Job.Type.JOB : Job.Type.CLASS;
	}

	private Job.Role role(int role) {
		return switch(role) {
			case 0 -> Job.Role.NON_BATTLE;
			case 1 -> Job.Role.TANK;
			case 2 -> Job.Role.MELEE_DPS;
			case 3 -> Job.Role.RANGED_DPS;
			case 4 -> Job.Role.HEALER;
			default -> throw new RuntimeException("Unknown role: " + role);
		};
	}

	private Attribute primaryStat(int primaryStat) {
		return switch(primaryStat) {
			case 0 -> null;
			case 1 -> Attribute.STRENGTH;
			case 2 -> Attribute.DEXTERITY;
			case 4 -> Attribute.INTELLIGENCE;
			case 5 -> Attribute.MIND;
			default -> throw new RuntimeException("Unknown attribute: " + primaryStat);
		};
	}
}
