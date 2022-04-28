package dev.stocky37.xiv.config;

import dev.stocky37.xiv.model.Ability;
import dev.stocky37.xiv.model.Job;
import dev.stocky37.xiv.model.Status;
import dev.stocky37.xiv.model.transform.DataMerger;
import dev.stocky37.xiv.util.Util;
import javax.inject.Named;
import javax.inject.Singleton;

public class DataMergeFactory {
	@Singleton
	@Named("abilities.merge")
	public DataMerger<Ability> forAbility(Util util) {
		return new DataMerger<>(util, Ability.class);
	}

	@Singleton
	@Named("statuses.merge")
	public DataMerger<Status> forStatus(Util util) {
		return new DataMerger<>(util, Status.class);
	}

	@Singleton
	@Named("jobs.merge")
	public DataMerger<Job> forJobs(Util util) {
		return new DataMerger<>(util, Job.class);
	}
}
