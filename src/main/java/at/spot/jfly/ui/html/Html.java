package at.spot.jfly.ui.html;

import at.spot.jfly.ui.base.AbstractContainerComponent;

public class Html extends AbstractContainerComponent {

	private Head head;
	private Body body;

	public Head head() {
		return head;
	}

	public Body body() {
		return body;
	}

	public Html head(Head head) {
		this.head = head;
		return this;
	}

	public Html body(Body body) {
		this.body = body;
		return this;
	}

}