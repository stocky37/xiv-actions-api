package dev.stocky37.xiv.model.transform;

import dev.stocky37.xiv.model.Status;
import java.util.function.BinaryOperator;
import javax.inject.Singleton;

@Singleton
public class StatusMerger implements BinaryOperator<Status> {
	@Override
	public Status apply(Status source, Status update) {
		final var builder = Status.builder(source);
		if(update.duration() != null) {
			builder.duration(update.duration());
		}
		if(update.maxDuration().isPresent()) {
			builder.maxDuration(update.maxDuration().get());
		}
		if(update.effects() != null) {
			builder.effects(update.effects());
		}
		return builder.build();
	}
}
