package at.spot.jfly.util;

import at.spot.jfly.ui.base.Component;

public class VueHelper {
	public static String bindAttributeValue(final Component component, final String propertyName) {
		final String bind = String.format("componentStates.%s.%s", component.getUuid(), propertyName);
		return bind;
	}

	public static String bindElementContent(final Component component, final String propertyName) {
		final String bind = String.format("{{componentStates.%s.%s}}", component.getUuid(), propertyName);
		return bind;
	}
}
