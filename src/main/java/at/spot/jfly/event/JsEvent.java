package at.spot.jfly.event;

public enum JsEvent implements EventType {
	click("click"),
	hover("hover"),
	input("input"),
	mousedown("mousedown"),
	mouseup("mouseup"),
	focus("focus"),
	blur("blur"),
	keydown("keydown"),
	change("change"),
	dblclick("dblclick"),
	mousemove("mousemove"),
	mouseover("mouseover"),
	mouseout("mouseout"),
	mousewheel("mousewheel"),
	keyup("keyup"),
	keypress("keypress"),
	textInput("textInput"),
	touchstart("touchstart"),
	touchmove("touchmove"),
	touchend("touchend"),
	touchcancel("touchcancel"),
	resize("resize"),
	scroll("scroll"),
	zoom("zoom"),
	select("select"),
	submit("submit"),
	reset("reset");

	private String identifier;

	private JsEvent(final String id) {
		this.identifier = id;
	}

	@Override
	public String toString() {
		return this.identifier;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}
}
