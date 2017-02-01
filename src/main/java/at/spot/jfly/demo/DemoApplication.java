package at.spot.jfly.demo;

import at.spot.jfly.ComponentController;
import at.spot.jfly.Window;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.style.ButtonStyle;
import at.spot.jfly.style.LabelStyle;
import at.spot.jfly.style.NavbarStyle;
import at.spot.jfly.ui.action.Button;
import at.spot.jfly.ui.action.LinkAction;
import at.spot.jfly.ui.display.Badge;
import at.spot.jfly.ui.display.Label;
import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.ui.navigation.NavBar;
import at.spot.jfly.ui.selection.SingleButtonDropDown;

public class DemoApplication extends Window {

	@Override
	protected Head createHeader() {
		final Head head = new Head().title("Hello world");

		return head;
	}

	@Override
	protected Body createBody() {
		final NavBar navBar = new NavBar(NavbarStyle.Inverse);

		navBar.header(new LinkAction("spOt"));
		navBar.addChildren(new LinkAction("Settings").location("#settings"));
		navBar.addChildren(new LinkAction("Logout").onEvent(JsEvent.click, (e) -> {
			ComponentController.instance().invokeFunctionCall("jfly", "reloadApp");
			ComponentController.instance().closeCurrentSession();
		}));

		final Body body = new Body().addChildren(navBar).addStyleClasses("hidden");
		final Button button = new Button("Say hello!").addStyleClasses(ButtonStyle.Success);
		body.addChildren(new SingleButtonDropDown("menu").addMenuItem(new LinkAction("test")));
		body.addChildren(button);
		body.addChildren(new Label("test").addStyleClasses(LabelStyle.Danger));
		body.addChildren(new Badge("42"));

		button.onEvent(JsEvent.click, e -> {
			button.text("clicked");
			// body.addChildren(TagCreator.h1("hello world"));
		});

		button.onEvent(JsEvent.mouseout, e -> {
			button.text("and out");
		});

		return body;
	}
}
