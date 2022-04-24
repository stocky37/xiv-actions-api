package dev.stocky37.xiv.model.transform;

import dev.stocky37.xiv.model.Attribute;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.util.Util;
import dev.stocky37.xiv.xivapi.json.XivClassJob;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class JobConverter implements Function<XivClassJob, Job> {

	private final Util util;

	@Inject
	public JobConverter(Util util) {
		this.util = util;
	}

	@Override
	public Job apply(XivClassJob classjob) {
		return Job.builder()
			.withId(classjob.ID().toString())
			.withName(classjob.Name())
			.withAbbreviation(classjob.Abbreviation())
			.withIcon(util.prefixXivApi(classjob.Icon()))
			.withCategory(category(classjob.ClassJobCategoryTargetID()))
			.withType(type(classjob.JobIndex()))
			.withRole(role(classjob.Role()))
			.withIndex(classjob.JobIndex())
			.withLimited(classjob.IsLimitedJob())
			.withPrimaryStat(primaryStat(classjob.PrimaryStat()))
			.build();
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
