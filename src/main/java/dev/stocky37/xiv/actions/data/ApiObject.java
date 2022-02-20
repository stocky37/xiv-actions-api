package dev.stocky37.xiv.actions.data;

import java.net.URI;

public interface ApiObject {

	String id();

	String name();

	URI icon();

	URI hdIcon();

	String description();
}
