package dev.stocky37.xiv.actions.data;

import com.fasterxml.jackson.annotation.JsonView;
import java.net.URI;

public interface ApiObject {
	String id();

	@JsonView(Views.Limited.class)
	String name();

	@JsonView(Views.Limited.class)
	URI icon();

	@JsonView(Views.Standard.class)
	URI hdIcon();

	@JsonView(Views.Standard.class)
	String description();
}
