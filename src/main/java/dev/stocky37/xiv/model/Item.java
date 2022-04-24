package dev.stocky37.xiv.model;

import com.fasterxml.jackson.annotation.JsonView;
import dev.stocky37.xiv.api.json.Views;
import dev.stocky37.xiv.util.Util;
import java.net.URI;

public interface Item extends ApiObject {
	@JsonView(Views.Standard.class)
	Kind kind();

	@JsonView(Views.Standard.class)
	URI hdIcon();

	@JsonView(Views.Standard.class)
	String description();

	enum Kind {
		ARM, TOOL, ARMOR, ACCESSORY, CONSUMABLE, MATERIAL, OTHER;

		@Override
		public String toString() {
			return Util.slugify(name());
		}
	}
}
