package io.spotnext.jfly.event;

import java.io.Serializable;

public interface EventHandler extends Serializable {
	void handle(DomEvent event);
}
