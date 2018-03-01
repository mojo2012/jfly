package at.spot.jfly.demo;

import at.spot.jfly.Application;
import at.spot.jfly.ClientCommunicationHandler;
import at.spot.jfly.Server;
import at.spot.jfly.Window;
import at.spot.jfly.style.GlyphIcon;
import at.spot.jfly.style.LabelStyle;
import at.spot.jfly.style.NavbarStyle;
import at.spot.jfly.style.NavigationTarget;
import at.spot.jfly.ui.GenericContainer;
import at.spot.jfly.ui.action.Button;
import at.spot.jfly.ui.action.LinkAction;
import at.spot.jfly.ui.base.GenericComponent;
import at.spot.jfly.ui.display.Badge;
import at.spot.jfly.ui.display.Label;
import at.spot.jfly.ui.display.VSpacer;
import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.ui.navigation.SideBar;
import at.spot.jfly.ui.navigation.SidebarNavContainer;
import at.spot.jfly.ui.navigation.SidebarNavEntry;
import at.spot.jfly.ui.navigation.ToolBar;
import at.spot.jfly.ui.selection.DropDownBox;
import j2html.TagCreator;

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

			// top nav bar
			final ToolBar toolBar = new ToolBar(application(), NavbarStyle.Inverse);

			toolBar.header(new Label(application(), "spOt"));
			toolBar.addAttributes("app absolute clipped-left");

			toolBar.addChildren(new VSpacer(application()));
			toolBar.addChildren(new LinkAction(application(), "Settings").location("#settings").onClick(e -> {
				// show settings dialog
			}));
			toolBar.addChildren(new LinkAction(application(), "Reload").onClick(e -> {
				application().destroy();
			}));

			body.addChildren(sidebar);
			body.addChildren(toolBar);

			// content

			GenericContainer mainContainer = new GenericContainer(application(), "v-content");
			GenericContainer fluidContainer = new GenericContainer(application(), "v-container");
			fluidContainer.addAttributes("fluid fill-height");
			GenericContainer actualContainer = new GenericContainer(application(), "v-layout");
			actualContainer.addAttributes("justify-center align-center");
			GenericContainer flexContainer = new GenericContainer(application(), "v-flex");
			flexContainer.addAttributes("shrink");

			mainContainer.addChildren(fluidContainer);
			fluidContainer.addChildren(actualContainer);
			actualContainer.addChildren(flexContainer);
			body.addChildren(mainContainer);

			final LinkAction linkAction = new LinkAction(application(), "google.at", "https://google.at",
					NavigationTarget.Blank);

			final Button button = new Button(application(), "Say hello!");
			button.onClick(e -> {
				button.text("clicked");
				actualContainer.addChildren(new GenericComponent(application(), TagCreator.h1("hello world")));
			});

			button.onMouseOut(e -> {
				button.text("and out");
			});

			DropDownBox dropdown = new DropDownBox(application(), "dropdown menu");
			dropdown.leftIcon(GlyphIcon.Map);

			// TODO: pushing data asynchronously doesn't work yet
			dropdown.addMenuItem("startUpdateTimer", "Start update timer", e -> {
				System.out.println("Starting timer");
				// new Thread(() -> {
				// for (byte b = 0; b < 4; b++) {
				// GenericComponent comp = new GenericComponent(application(),
				// TagCreator.h2("Time: " + System.currentTimeMillis()));
				// actualContainer.addChildren(comp);
				// try {
				// Thread.sleep(1000);
				// } catch (InterruptedException e1) {
				// e1.printStackTrace();
				// }
				// }
				// }).start();
			});
			dropdown.addMenuItem("test2", "test 2", e -> {
				dropdown.addMenuItem("addedItem" + dropdown.menuItems().size(), "This has been added manually", null);
			});

			actualContainer.addChildren(linkAction);
			actualContainer.addChildren(dropdown);
			actualContainer.addChildren(button);
			actualContainer.addChildren(new Label(application(), "test").addStyleClasses(LabelStyle.Danger));
			actualContainer.addChildren(new Badge(application(), "42"));

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
