package at.spot.jfly.templating;

import java.util.Map;

import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.util.KeyValueMapping;

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

	public Map<String, Object> getValues() {
		return values;
	}
}
