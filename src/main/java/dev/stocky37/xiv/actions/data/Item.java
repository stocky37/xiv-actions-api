package dev.stocky37.xiv.actions.data;

import static dev.stocky37.xiv.actions.util.Util.slugify;

public interface Item extends ApiObject {
	Kind kind();

	enum Kind {
		ARM, TOOL, ARMOR, ACCESSORY, CONSUMABLE, MATERIAL, OTHER;

		@Override
		public String toString() {
			return slugify(name());
		}
	}
}
