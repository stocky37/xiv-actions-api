package dev.stocky37.xiv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.stocky37.xiv.util.Util;

public enum Attribute {
	STRENGTH,
	DEXTERITY,
	VITALITY,
	INTELLIGENCE,
	MIND,
	PIETY,
	PHYSICAL_DAMAGE,
	MAGIC_DAMAGE,
	DELAY,
	ATTACK_SPEED,
	TENACITY,
	ATTACK_POWER,
	DIRECT_HIT_RATE,
	MAGIC_DEFENSE,
	CRITICAL_HIT,
	DETERMINATION,
	SKILL_SPEED,
	SPELL_SPEED;

	@JsonCreator
	public static Attribute fromString(String str) {
		for(final var attr : Attribute.values()) {
			if(attr.toString().equalsIgnoreCase(str)) {
				return attr;
			}
		}
		throw new IllegalArgumentException("Invalid attribute: " + str);
	}

	@Override
	public String toString() {
		return Util.slugify(name());
	}
}
