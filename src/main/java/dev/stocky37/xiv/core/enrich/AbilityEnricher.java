package dev.stocky37.xiv.core.enrich;

import dev.stocky37.xiv.model.Ability;
import dev.stocky37.xiv.model.transform.AbilityMerger;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class AbilityEnricher extends DataEnricher<Ability> {

	@Inject
	public AbilityEnricher(@Named("data.actions") Map<String, Ability> data, AbilityMerger merger) {
		super(data, merger);
	}
}
