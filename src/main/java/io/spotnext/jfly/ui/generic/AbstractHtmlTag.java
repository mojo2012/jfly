package io.spotnext.jfly.ui.generic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;

public abstract class AbstractHtmlTag extends AbstractComponent {

	private String tagName;
	private final Map<String, String> attributes = new HashMap<>();

	public AbstractHtmlTag(final ComponentHandler handler, String tagName) {
		super(handler);
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
		updateClientComponent();
	}

	public Map<String, String> getHtmlAttributes() {
		return Collections.unmodifiableMap(attributes);
	}

	public void addHtmlAttribute(String name, String value) {
		attributes.put(name, value);
		updateClientComponent();
	}
}
