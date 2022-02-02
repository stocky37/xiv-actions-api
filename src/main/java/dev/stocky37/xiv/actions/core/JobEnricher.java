package dev.stocky37.xiv.actions.core;

import dev.stocky37.xiv.actions.data.Action;
import dev.stocky37.xiv.actions.data.Item;
import dev.stocky37.xiv.actions.data.Job;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JobEnricher implements UnaryOperator<Job> {

	private final ActionService actions;
	private final ItemService items;

	@Inject
	public JobEnricher(ActionService actions, ItemService items) {
		this.actions = actions;
		this.items = items;
	}

	@Override
	public Job apply(Job job) {
		return new Job(job, actions(job), potions(job));
	}

	private List<Action> actions(Job job) {
		return Collections.unmodifiableList(actions.findForJob(job));
	}

	private List<Item> potions(Job job) {
		return Collections.unmodifiableList(items.findPotionsForJob(job));
	}
}
