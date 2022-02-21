package dev.stocky37.xiv.actions.core;

import dev.stocky37.xiv.actions.data.Ability;
import dev.stocky37.xiv.actions.data.Consumable;
import dev.stocky37.xiv.actions.data.Job;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JobEnricher implements UnaryOperator<Job> {

	private final AbilityService abilities;
	private final ItemService items;

	@Inject
	public JobEnricher(AbilityService abilities, ItemService items) {
		this.abilities = abilities;
		this.items = items;
	}

	@Override
	public Job apply(Job job) {
		return Job.builder(job)
			.withActions(actions(job))
			.withPotions(potions(job))
			.build();
	}

	private List<Ability> actions(Job job) {
		return Collections.unmodifiableList(abilities.findForJob(job));
	}

	private List<Consumable> potions(Job job) {
		return Collections.unmodifiableList(items.findPotionsForJob(job));
	}
}
