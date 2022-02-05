package dev.stocky37.xiv.actions.util;

import com.github.slugify.Slugify;

public class Util {
	private Util() {}

	private static final Slugify slugifier = new Slugify().withCustomReplacement("_", "-");

	public static String slugify(String text) {
		return slugifier.slugify(text);
	}
}
