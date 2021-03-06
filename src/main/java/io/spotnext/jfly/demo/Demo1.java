package io.spotnext.jfly.demo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.spotnext.jfly.Server;
import io.spotnext.jfly.SinglePageApplication;
import io.spotnext.jfly.attributes.Attributes.GridAlignment;
import io.spotnext.jfly.attributes.Attributes.GridBehavior;
import io.spotnext.jfly.attributes.Attributes.HorizontalOrientation;
import io.spotnext.jfly.attributes.Attributes.TextFieldStyle;
import io.spotnext.jfly.attributes.MaterialIcon;
import io.spotnext.jfly.attributes.NavigationTarget;
import io.spotnext.jfly.attributes.Styles.Color;
import io.spotnext.jfly.ui.action.Button;
import io.spotnext.jfly.ui.action.LinkAction;
import io.spotnext.jfly.ui.data.Column;
import io.spotnext.jfly.ui.data.DataTable;
import io.spotnext.jfly.ui.data.DataTableRow;
import io.spotnext.jfly.ui.display.Badge;
import io.spotnext.jfly.ui.display.Icon;
import io.spotnext.jfly.ui.display.Label;
import io.spotnext.jfly.ui.display.Spacer;
import io.spotnext.jfly.ui.generic.GenericContainer;
import io.spotnext.jfly.ui.generic.SimpleHtmlTag;
import io.spotnext.jfly.ui.html.Body;
import io.spotnext.jfly.ui.html.Head;
import io.spotnext.jfly.ui.input.TextField;
import io.spotnext.jfly.ui.navigation.Drawer;
import io.spotnext.jfly.ui.navigation.SideBar;
import io.spotnext.jfly.ui.navigation.ToolBar;
import io.spotnext.jfly.ui.navigation.TreeNode;
import io.spotnext.jfly.ui.navigation.TreeView;
import io.spotnext.jfly.ui.navigation.TreeView.NodeType;
import io.spotnext.jfly.ui.selection.DropDownBox;
import io.spotnext.jfly.util.Localizable;
import io.spotnext.jfly.util.LocalizationBundle;

public class Demo1 extends SinglePageApplication {
	private static final Logger LOG = LoggerFactory.getLogger(Server.class);

	private final static LocalizationBundle localizations = new LocalizationBundle("messages/demo_messages");

	@Override
	protected Head createHeader() {
		return new Head(getHandler()).setTitle("jfly UI framework");
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
		fluidContainer.addAttribute(GridBehavior.Fluid);
		fluidContainer.addAttribute(GridBehavior.FillHeight);
		final GenericContainer actualContainer = new GenericContainer(getHandler(), "v-layout");
		actualContainer.addAttribute(GridBehavior.JustifyCenter);
		actualContainer.addAttribute(GridAlignment.Center);
		actualContainer.setUseWrapper(true);
		actualContainer.addStyleClass("container");

		mainContainer.addChildren(fluidContainer);
		fluidContainer.addChildren(actualContainer);
		body.addChildren(mainContainer);

		TextField multiLineTextBox = new TextField(getHandler(), null);
		// multiLineTextBox.setPlaceholder("Enter text here ...");
		multiLineTextBox.setMultiLine(true);
		multiLineTextBox.setLabel(localizations.forKey("placeholders.story"));
		multiLineTextBox.setText("This is a test text");
		multiLineTextBox.onChange(e -> System.out.print("Entered text: " + e.getData().get("value")));

		final DataTable<DataTableObject> dataTable = new DataTable<>(getHandler());
		dataTable.setAllowSelect(true);
		dataTable.setAllowSelectAll(true);
		dataTable.setNoDataText(Localizable.of("Empty"));
		dataTable.setRowsPerPageText(Localizable.of("Max. rows per page"));
		dataTable.addColumn(new Column("title", "Title"));
		dataTable.addColumn(new Column("value", "Value"));
		dataTable.getColumns().get(0).setSortable(true);

		final TextField filterDataTableBox = new TextField(getHandler(), null);
		filterDataTableBox.setPlaceholder(localizations.forKey("placeholders.search"));
		filterDataTableBox.onChange(e -> dataTable.setFilterText((String) e.getData().get("data")));

		for (int x = 0; x < 100; x++) {
			dataTable.addChildren(new DataTableObject(x + "", "Test" + x, x + "500,--"));
		}

		final LinkAction linkAction = new LinkAction(getHandler(), Localizable.of("google.at"), "https://google.at",
				NavigationTarget.Blank);

		final Button button = new Button(getHandler(), Localizable.of("Say hello!"));
		button.onClick(e -> {
			button.setText(Localizable.of("clicked"));

			Label label = new Label(getHandler(), Localizable.of("Current time: " + new Date().toString()));
			actualContainer.addChildren(label);
		});

		button.onMouseOut(e -> {
			button.setText(Localizable.of("and out"));
		});

		DropDownBox dropdown = new DropDownBox(getHandler(), Localizable.of("Please choose ..."));
		dropdown.setLeftIcon(MaterialIcon.map);

		dropdown.addMenuItem("startUpdateTimer", "Start update timer", e -> {
			LOG.debug("Starting timer");
			new Thread(() -> {
				for (byte b = 0; b < 4; b++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					SimpleHtmlTag comp = new SimpleHtmlTag(getHandler(), "h2");
					comp.setContent(("Time: " + System.currentTimeMillis()));
					actualContainer.addChildren(comp);

					flushClientUpdates();
				}
			}).start();
		});
		dropdown.addMenuItem("test2", "test 2", e -> {
			dropdown.addMenuItem("addedItem" + dropdown.getMenuItems().size(), "This has been added manually", null);
		});

		Button causeExceptionButton = new Button(getHandler(), Localizable.of("Cause exception"));
		causeExceptionButton.setFlat(true);
		causeExceptionButton.onClick(e -> {
			throw new RuntimeException("Custom exception.");
		});

		actualContainer.addChildren(filterDataTableBox, dataTable, multiLineTextBox, linkAction, dropdown,
				causeExceptionButton, button);

		Label label = new Label(getHandler(), Localizable.of("Danger"));
		label.addStyleClass(Color.RED);
		actualContainer.addChildren(label);
		actualContainer.addChildren(new Badge(getHandler(), Localizable.of("42")));

	}

	protected void createNavigation(Body body) {
		Spacer vSpacer = new Spacer(getHandler());

		// sidebar

		SideBar sidebar = new SideBar(getHandler());

		Drawer rightDrawer = new Drawer(getHandler(), HorizontalOrientation.Right);
		rightDrawer.setToolBar(new ToolBar(getHandler()));
		rightDrawer.getToolBar().setHeader(new Label(getHandler(), Localizable.of("Settings")));
		rightDrawer.setVisible(false);

		Button drawerCloseButton = new Button(getHandler());
		drawerCloseButton.setIcon(new Icon(getHandler(), MaterialIcon.close));
		drawerCloseButton.setFlat(true);
		drawerCloseButton.onClick(e -> rightDrawer.setVisible(false));
		rightDrawer.getToolBar().addChildren(vSpacer);
		rightDrawer.getToolBar().addChildren(drawerCloseButton);

		// tree navigation view
		TreeView treeView = new TreeView(getHandler());
		sidebar.addChildren(treeView);

		TreeNode infoNode = new TreeNode(getHandler(), localizations.forKey("action.info"));
		infoNode.setIcon(new Icon(getHandler(), MaterialIcon.info));
		infoNode.onClick(e -> System.out.println("info clicked"));

		TreeNode splitter = new TreeNode(getHandler(), null);
		splitter.setNodeType(NodeType.SPLITTER);

		TreeNode apiDocumentation = new TreeNode(getHandler(), Localizable.of("API documentation"));
		apiDocumentation.setNodeType(NodeType.SUB_HEADER);

		TreeNode componentNode = new TreeNode(getHandler(), Localizable.of("Components"));
		componentNode.setIcon(new Icon(getHandler(), MaterialIcon.widgets));
		componentNode.setExpanded(true);
		componentNode.setBadge(Localizable.of("new"));
		TreeNode selectionComponentNode = new TreeNode(getHandler(), Localizable.of("Status"));
		selectionComponentNode.setSubTitle(Localizable.of("Radio buttons, checkboxes ..."));
		selectionComponentNode.setIcon(new Icon(getHandler(), MaterialIcon.select_all));

		TreeNode demoComponentNode = new TreeNode(getHandler(), Localizable.of("Demo"));
		demoComponentNode.setIcon(new Icon(getHandler(), MaterialIcon.ac_unit));
		selectionComponentNode.addChildren(demoComponentNode);

		componentNode.addChildren(selectionComponentNode);
		treeView.addChildren(infoNode, splitter, apiDocumentation, componentNode);

		// top nav bar
		final ToolBar toolBar = new ToolBar(getHandler());
		toolBar.setHeader(new Label(getHandler(), Localizable.of("spOt")));

		TextField searchBox = new TextField(getHandler());
		searchBox.setPlaceholder(localizations.forKey("placeholders.search"));
		searchBox.setFlat(true);
		searchBox.addAttribute(TextFieldStyle.Solo);
		toolBar.addChildren(searchBox);

		Button reloadButton = new Button(getHandler(), Localizable.of("Reload"));
		reloadButton.setFlat(true);
		reloadButton.setIcon(new Icon(getHandler(), MaterialIcon.refresh));
		reloadButton.onClick(e -> {
			getHandler().destroy();
		});

		Button settingsButton = new Button(getHandler());
		settingsButton.setFlat(true);
		settingsButton.setIcon(new Icon(getHandler(), MaterialIcon.settings));
		settingsButton.onClick(e -> {
			rightDrawer.setVisible(!rightDrawer.isVisible());
		});

		toolBar.addChildren(reloadButton);
		toolBar.addChildren(settingsButton);

		body.addChildren(sidebar);
		body.addChildren(rightDrawer);
		body.addChildren(toolBar);
	}

	@Override
	protected List<Locale> getSupportedLocales() {
		return Arrays.asList(Locale.GERMAN, Locale.ENGLISH);
	}

	/**
	 * Entry point for demo application
	 */
	public static void main(final String[] args) {
		final Server server = new Server(8080);
		server.registerViewHandler(Arrays.asList("/"), Demo1.class);
		server.start();
	}

	@Override
	public void route(String url, boolean flushChanges) {

	}

	public static class DataTableObject implements DataTableRow {
		String id;
		String title;
		String value;
		boolean selected = false;

		public DataTableObject(String id, String title, String value) {
			this.id = id;
			this.title = title;
			this.value = value;
		}

		public String getTitle() {
			return title;
		}

		public String getValue() {
			return value;
		}

		@Override
		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		@Override
		public boolean isSelected() {
			return selected;
		}

		@Override
		public String getId() {
			return this.id;
		}
	}
}
