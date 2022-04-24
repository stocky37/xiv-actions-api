package dev.stocky37.xiv.actions.model;

import static dev.stocky37.xiv.actions.util.Util.slugify;

import com.fasterxml.jackson.annotation.JsonView;
import dev.stocky37.xiv.actions.json.Views;
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
			return slugify(name());
		}
	}
}
