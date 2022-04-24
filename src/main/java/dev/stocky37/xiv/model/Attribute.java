package dev.stocky37.xiv.model;

import dev.stocky37.xiv.util.Util;

public enum Attribute {
	STRENGTH, DEXTERITY, VITALITY, INTELLIGENCE, MIND;

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
