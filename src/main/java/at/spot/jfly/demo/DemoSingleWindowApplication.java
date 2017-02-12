package at.spot.jfly.demo;

import at.spot.jfly.Application;
import at.spot.jfly.ClientCommunicationHandler;
import at.spot.jfly.Server;
import at.spot.jfly.Window;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.style.NavbarStyle;
import at.spot.jfly.ui.action.LinkAction;
import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.ui.navigation.NavBar;
import at.spot.jfly.ui.navigation.SideBar;
import at.spot.jfly.ui.navigation.SidebarNavContainer;
import at.spot.jfly.ui.navigation.SidebarNavEntry;

public class DemoSingleWindowApplication extends Application {

	protected Window window;

	@Override
	public <A extends Application> A init(final ClientCommunicationHandler handler, final String sessionId) {
		super.init(handler, sessionId);
		window = new SingleWindow(this);
		return (A) this;
	}

	@Override
	public String render(final String urlPath) {
		// we return the same window for all urls
		return window.render();
	}

	public static class SingleWindow extends Window {
		public SingleWindow(final Application application) {
			super(application);
		}

		@Override
		protected Head createHeader() {
			final Head head = new Head(application()).title("Hello world");

			return head;
		}

		@Override
		protected Body createBody() {
			final Body body = new Body(application());

			// sidebar
			SidebarNavContainer navContainer = new SidebarNavContainer(application());
			navContainer.title("Open files");
			navContainer.addChildren(new SidebarNavEntry(application(), "Test"));

			SideBar sidebar = new SideBar(application());
			sidebar.addChildren(navContainer);

			body.addChildren(sidebar);

			// top nav bar
			final NavBar navBar = new NavBar(application(), NavbarStyle.Inverse);

			navBar.header(new LinkAction(application(), "spOt"));
			navBar.addChildren(new LinkAction(application(), "Settings").location("#settings"));
			navBar.addChildren(new LinkAction(application(), "Logout").onEvent(JsEvent.click, (e) -> {
				application().invokeFunctionCall("jfly", "reloadApp");
			}));

			body.addChildren(navBar);

			// content

			// final Button button = new Button(application(), "Say
			// hello!").addStyleClasses(ButtonStyle.CocoaCapsule);
			// body.addChildren(
			// new SingleButtonDropDown(application(), "menu").addMenuItem(new
			// LinkAction(application(), "test"))
			// .addStyleClasses(ButtonStyle.CocoaCapsule));
			// body.addChildren(button);
			// body.addChildren(new Label(application(),
			// "test").addStyleClasses(LabelStyle.Danger));
			// body.addChildren(new Badge(application(), "42"));

			// button.onEvent(JsEvent.click, e -> {
			// button.text("clicked");
				// body.addChildren(new GenericComponent(application(),
				// TagCreator.h1("hello world")));
			// });

			// button.onEvent(JsEvent.mouseout, e -> {
			// button.text("and out");
			// });

			return body;
		}
	}

	/**
	 * Entry point for demo application
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		final Server server = new Server(DemoSingleWindowApplication.class, 8080);
		server.init();
	}
}
