package io.spotnext.jfly.templating;

import java.util.Map;

import io.spotnext.jfly.ui.base.AbstractComponent;
import io.spotnext.jfly.util.KeyValueMapping;

public class ComponentContext {
	private AbstractComponent component;
	private final KeyValueMapping<String, Object> values = new KeyValueMapping<>();

	public AbstractComponent getComponent() {
		return component;
	}

	public void setComponent(AbstractComponent component) {
		this.component = component;
		put("component", component);
	}

	public void put(String key, Object value) {
		values.put(key, value);
	}

	public void putAll(Map<String, Object> values) {
		values.putAll(values);
	}

	public Map<String, Object> getValues() {
		return values;
	}
}
