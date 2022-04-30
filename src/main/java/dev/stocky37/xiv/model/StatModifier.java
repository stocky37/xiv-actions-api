package dev.stocky37.xiv.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Optional;

@JsonTypeName("stat")
public record StatModifier(Attribute attribute, double multiplier, Optional<Integer> max)
	implements Status.Effect<Stats> {

	@Override
	public Status.Type type() {
		return Status.Type.STAT;
	}

	@Override
	public Stats modify(Stats stats) {
		final var builder = Stats.builder(stats);
		switch(attribute) {
			case STRENGTH -> builder.strength(applyMod(stats.strength()));
			case DEXTERITY -> builder.dexterity(applyMod(stats.dexterity()));
			case INTELLIGENCE -> builder.intelligence(applyMod(stats.intelligence()));
			case MIND -> builder.mind(applyMod(stats.mind()));
			case TENACITY -> builder.tenacity(applyMod(stats.tenacity()));
			case CRITICAL_HIT -> builder.crit(applyMod(stats.crit()));
			case DETERMINATION -> builder.determination(applyMod(stats.determination()));
			case SKILL_SPEED -> builder.skillSpeed(applyMod(stats.skillSpeed()));
			case SPELL_SPEED -> builder.spellSpeed(applyMod(stats.spellSpeed()));
		}
		return builder.build();
	}

	private int applyMod(int stat) {
		final int newStat = (int) (stat * multiplier);
		return max.map(integer -> Math.min(newStat, stat + integer)).orElse(newStat);
	}
}
