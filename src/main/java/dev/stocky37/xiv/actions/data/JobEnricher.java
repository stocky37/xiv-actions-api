package dev.stocky37.xiv.actions.data;

import dev.stocky37.xiv.actions.core.ActionService;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import javax.inject.Singleton;

@Singleton
public class JobEnricher implements UnaryOperator<Job> {

	private final ActionService actionService;

	public JobEnricher(ActionService actionService) {
		this.actionService = actionService;
	}

	@Override
	public Job apply(Job job) {
		return new Job(job, actions(job.abbreviation()));
	}

	private List<Action> actions(String jobAbbreviation) {
		return Collections.unmodifiableList(actionService.findForJob(jobAbbreviation));
	}

}
