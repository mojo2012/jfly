package at.spot.jfly.viewhandlers;

import at.spot.jfly.ViewHandler;
import at.spot.jfly.ui.generic.GenericContainer;
import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.ui.input.TextField;

public class ExceptionViewHandler extends ViewHandler {

	@Override
	protected Head createHeader() {
		final Head head = new Head(getHandler()).setTitle("An error occurred");

		return head;
	}

	@Override
	protected Body createBody() {
		Body body = new Body(this);

		TextField errorHeader = new TextField(this);
		errorHeader.setText("Sorry, an error occurred");

		GenericContainer container = new GenericContainer(this, "div");
		container.addChildren(errorHeader);

		body.addChildren(container);

		return body;
	}

}
