package at.spot.jfly.ui.html;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractContainerComponent;

public class Html extends AbstractContainerComponent {

	private transient Head head;
	private transient Body body;

	public Html(final ComponentHandler handler) {
		super(handler);
	}

	public Head head() {
		return head;
	}

	public Body body() {
		return body;
	}

	public Html head(final Head head) {
		this.head = head;
		return this;
	}

	public Html body(final Body body) {
		this.body = body;
		return this;
	}

}