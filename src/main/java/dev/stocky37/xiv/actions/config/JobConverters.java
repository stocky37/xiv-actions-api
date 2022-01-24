package dev.stocky37.xiv.actions.config;

import dev.stocky37.xiv.actions.core.ActionService;
import dev.stocky37.xiv.actions.data.XivApiJobConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@SuppressWarnings("CdiInjectInspection")
public class JobConverters {

	@Produces
	@Named("unenriched")
	@ApplicationScoped
	public XivApiJobConverter unenrichedJobConverter(ActionService actions) {
		return new XivApiJobConverter(actions, false);
	}

	@Produces
	@Named("enriched")
	@ApplicationScoped
	public XivApiJobConverter enrichedJobConverter(ActionService actions) {
		return new XivApiJobConverter(actions, true);
	}
}
