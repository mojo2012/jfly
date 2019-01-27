package io.spotnext.jfly.ui.html;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;
import io.spotnext.jfly.ui.base.AbstractContainerComponent;

public class Html extends AbstractContainerComponent<AbstractComponent> {

	private Head head;
	private Body body;

	public Html(final ComponentHandler handler) {
		super(handler);
	}

	public Head getHead() {
		return head;
	}

	public Body getBody() {
		return body;
	}

	public void setHead(final Head head) {
		this.head = head;
	}

	public void setBody(final Body body) {
		this.body = body;
	}

}