package dev.stocky37.xiv.actions.model;

import com.fasterxml.jackson.annotation.JsonView;
import dev.stocky37.xiv.actions.json.Views;
import dev.stocky37.xiv.actions.util.Util;
import java.net.URI;

public interface ApiObject {
	String id();

	@JsonView(Views.Limited.class)
	String name();

	@JsonView(Views.Limited.class)
	URI icon();

	@JsonView(Views.Limited.class)
	default String slug() {
		return Util.slugify(name());
	}
}
