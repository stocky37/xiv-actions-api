package dev.stocky37.xiv.core.enrich;

import com.fasterxml.jackson.databind.JsonNode;
import dev.stocky37.xiv.core.AbilityService;
import dev.stocky37.xiv.core.ItemService;
import dev.stocky37.xiv.model.Ability;
import dev.stocky37.xiv.model.Consumable;
import dev.stocky37.xiv.model.Job;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class JobEnricher extends MergingEnricher<Job> {

	private final AbilityService abilities;
	private final ItemService items;

	@Inject
	public JobEnricher(
		@Named("jobs.data") JsonNode data,
		@Named("jobs.merge") BiFunction<Job, JsonNode, Job> merger,
		AbilityService abilities,
		ItemService items
	) {
		super(data, merger);
		this.abilities = abilities;
		this.items = items;
	}

	@Override
	protected Job enrich(Job job, JsonNode update) {
		return Job.builder(job)
			.withActions(actions(job))
			.withPotions(potions(job))
			.build();
	}

	private List<Ability> actions(Job job) {
		return abilities.findForJob(job);
	}

	private List<Consumable> potions(Job job) {
		return items.findPotionsForJob(job);
	}
}
