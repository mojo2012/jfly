package at.spot.jfly.demo;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.spot.jfly.Application;
import at.spot.jfly.ClientCommunicationHandler;
import at.spot.jfly.Server;
import at.spot.jfly.View;
import at.spot.jfly.style.GlyphIcon;
import at.spot.jfly.style.HorizontalOrientation;
import at.spot.jfly.style.LabelStyle;
import at.spot.jfly.style.MaterialIcon;
import at.spot.jfly.style.NavbarStyle;
import at.spot.jfly.style.NavigationTarget;
import at.spot.jfly.ui.action.Button;
import at.spot.jfly.ui.action.LinkAction;
import at.spot.jfly.ui.display.Badge;
import at.spot.jfly.ui.display.Icon;
import at.spot.jfly.ui.display.Label;
import at.spot.jfly.ui.display.VSpacer;
import at.spot.jfly.ui.generic.GenericContainer;
import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.ui.input.TextField;
import at.spot.jfly.ui.navigation.Drawer;
import at.spot.jfly.ui.navigation.SideBar;
import at.spot.jfly.ui.navigation.ToolBar;
import at.spot.jfly.ui.navigation.TreeNode;
import at.spot.jfly.ui.navigation.TreeView;
import at.spot.jfly.ui.navigation.TreeView.NodeType;
import at.spot.jfly.ui.selection.DropDownBox;

public class DemoSingleViewApplication extends Application {
	private static final Logger LOG = LoggerFactory.getLogger(Server.class);

	protected View window;

	@Override
	public void init(final ClientCommunicationHandler handler, final String sessionId) {
		super.init(handler, sessionId);
		window = new SingleView(this);
	}

	@Override
	public String render(final String urlPath) {
		// we return the same window for all urls
		return window.render();
	}

	public static class SingleView extends View {
		public SingleView(final Application application) {
			super(application);
		}

		@Override
		protected Head createHeader() {
			final Head head = new Head(getHandler()).setTitle("jfly UI framework");

			return head;
		}

		@Override
		protected Body createBody() {
			final Body body = new Body(getHandler());

			createNavigation(body);
			createMainContent(body);

			return body;
		}

		private void createMainContent(Body body) {
			GenericContainer mainContainer = new GenericContainer(getHandler(), "v-content");
			GenericContainer fluidContainer = new GenericContainer(getHandler(), "v-container");
			fluidContainer.addAttribute("fluid", null);
			fluidContainer.addAttribute("fill-height", null);
			GenericContainer actualContainer = new GenericContainer(getHandler(), "v-layout");
			actualContainer.addAttribute("justify-center", null);
			actualContainer.addAttribute("align-center", null);
			actualContainer.setUseWrapper(true);
			actualContainer.addStyleClasses("container");

			mainContainer.addChildren(fluidContainer);
			fluidContainer.addChildren(actualContainer);
			body.addChildren(mainContainer);

			TextField singleLineTextBox = new TextField(getHandler(), null);
			singleLineTextBox.setPlaceholder("Search ...");
			// singleLineTextBox.setLabel("Quick search");

			TextField multiLineTextBox = new TextField(getHandler(), null);
			// multiLineTextBox.setPlaceholder("Enter text here ...");
			multiLineTextBox.setMultiLine(true);
			multiLineTextBox.setLabel("Story");
			multiLineTextBox.setText("This is a test text");
			multiLineTextBox.onChange(e -> System.out.print("Entered text: " + e.getPayload().get("value")));

			final LinkAction linkAction = new LinkAction(getHandler(), "google.at", "https://google.at",
					NavigationTarget.Blank);

			final Button button = new Button(getHandler(), "Say hello!");
			button.onClick(e -> {
				button.text("clicked");

				Label label = new Label(getHandler(), "Current time: " + new Date().toString());
				actualContainer.addChildren(label);
			});

			button.onMouseOut(e -> {
				button.text("and out");
			});

			DropDownBox dropdown = new DropDownBox(getHandler(), "dropdown menu");
			dropdown.setLeftIcon(GlyphIcon.Map);

			// TODO: pushing data asynchronously doesn't work yet
			dropdown.addMenuItem("startUpdateTimer", "Start update timer", e -> {
				LOG.debug("Starting timer");
				// new Thread(() -> {
				// for (byte b = 0; b < 4; b++) {
				// GenericComponent comp = new GenericComponent(getHandler(),
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
				dropdown.addMenuItem("addedItem" + dropdown.getMenuItems().size(), "This has been added manually",
						null);
			});

			actualContainer.addChildren(singleLineTextBox);
			actualContainer.addChildren(multiLineTextBox);
			actualContainer.addChildren(linkAction);
			actualContainer.addChildren(dropdown);
			actualContainer.addChildren(button);
			actualContainer.addChildren(new Label(getHandler(), "Danger").addStyleClasses(LabelStyle.Danger));
			actualContainer.addChildren(new Badge(getHandler(), "42"));

		}

		protected void createNavigation(Body body) {
			VSpacer vSpacer = new VSpacer(getHandler());

			// sidebar

			SideBar sidebar = new SideBar(getHandler());

			Drawer rightDrawer = new Drawer(getHandler(), HorizontalOrientation.Right);
			rightDrawer.setToolBar(new ToolBar(getHandler(), NavbarStyle.Default));
			rightDrawer.getToolBar().setHeader(new Label(getHandler(), "Settings"));
			rightDrawer.setVisibe(false);

			Button drawerCloseButton = new Button(getHandler());
			drawerCloseButton.setIcon(new Icon(getHandler(), MaterialIcon.close));
			drawerCloseButton.setFlat(true);
			drawerCloseButton.onClick(e -> rightDrawer.setVisibe(false));
			rightDrawer.getToolBar().addChildren(vSpacer);
			rightDrawer.getToolBar().addChildren(drawerCloseButton);

			// tree navigation view
			TreeView treeView = new TreeView(getHandler());
			sidebar.addChildren(treeView);

			TreeNode infoNode = new TreeNode(getHandler(), "Info");
			infoNode.setIcon(new Icon(getHandler(), MaterialIcon.info));

			TreeNode splitter = new TreeNode(getHandler(), null);
			splitter.setNodeType(NodeType.SPLITTER);

			TreeNode apiDocumentation = new TreeNode(getHandler(), "API documentation");
			apiDocumentation.setNodeType(NodeType.SUB_HEADER);

			TreeNode componentNode = new TreeNode(getHandler(), "Components");
			componentNode.setIcon(new Icon(getHandler(), MaterialIcon.widgets));
			componentNode.setExpanded(true);
			componentNode.setBadge("new");
			TreeNode selectionComponentNode = new TreeNode(getHandler(), "Status");
			selectionComponentNode.setSubTitle("Radio buttons, checkboxes ...");
			selectionComponentNode.setIcon(new Icon(getHandler(), MaterialIcon.select_all));

			// TreeNode demoComponentNode = new TreeNode(getHandler(), "Demo");
			// selectionComponentNode.addChildren(demoComponentNode);

			componentNode.addChildren(selectionComponentNode);
			treeView.addChildren(infoNode, splitter, apiDocumentation, componentNode);

			// top nav bar
			final ToolBar toolBar = new ToolBar(getHandler(), NavbarStyle.Inverse);
			toolBar.setHeader(new Label(getHandler(), "spOt"));

			toolBar.setLeftActionItem(e -> sidebar.setVisibe(!sidebar.isVisible()));
			toolBar.addChildren(vSpacer);

			Button reloadButton = new Button(getHandler(), "Reload");
			reloadButton.setFlat(true);
			reloadButton.onClick(e -> {
				getHandler().destroy();
			});

			Button settingsButton = new Button(getHandler());
			settingsButton.setFlat(true);
			settingsButton.setIcon(new Icon(getHandler(), MaterialIcon.settings));
			settingsButton.onClick(e -> {
				rightDrawer.setVisibe(!rightDrawer.isVisible());
			});

			toolBar.addChildren(reloadButton);
			toolBar.addChildren(settingsButton);

			body.addChildren(sidebar);
			body.addChildren(rightDrawer);
			body.addChildren(toolBar);
		}
	}

	/**
	 * Entry point for demo application
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		final Server server = new Server(DemoSingleViewApplication.class, 8080);
		server.init();
	}
}
