package dev.stocky37.xiv.actions.data;

import dev.stocky37.xiv.actions.core.ActionService;
import dev.stocky37.xiv.actions.xivapi.json.XivApiClassJob;
import java.util.Collections;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class XivApiJobConverter implements Function<XivApiClassJob, Job> {

	private final ActionService actionService;

	@Inject
	public XivApiJobConverter(ActionService actionService) {
		this.actionService = actionService;
	}

	@Override
	public Job apply(XivApiClassJob classJob) {
		return new Job(
			String.valueOf(classJob.ID()),
			classJob.Name(),
			classJob.Abbreviation(),
			classJob.Icon(),
			Collections.unmodifiableList(actionService.findForJob(classJob.Abbreviation()))
		);
	}
}
