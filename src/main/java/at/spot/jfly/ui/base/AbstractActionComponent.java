package at.spot.jfly.ui.base;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.style.GlyphIcon;

public class AbstractActionComponent extends AbstractTextComponent {
	private GlyphIcon icon;

	public AbstractActionComponent(final ComponentHandler handler, final String text) {
		super(handler, text);
	}

	public <C extends AbstractActionComponent> C glyphIcon(final GlyphIcon icon) {
		this.icon = icon;
		updateClientComponent();
		return (C) this;
	}

	public GlyphIcon glyphIcon() {
		return icon;
	}

	public <C extends AbstractActionComponent> C onClick(final EventHandler handler) {
		onEvent(JsEvent.click, handler);

		return (C) this;
	}

	public <C extends AbstractActionComponent> C onMouseOut(final EventHandler handler) {
		onEvent(JsEvent.mouseout, handler);

		return (C) this;
	}

	public <C extends AbstractActionComponent> C onMouseMove(final EventHandler handler) {
		onEvent(JsEvent.mousemove, handler);

		return (C) this;
	}

	public <C extends AbstractActionComponent> C onMouseDown(final EventHandler handler) {
		onEvent(JsEvent.mousedown, handler);

		return (C) this;
	}

	public <C extends AbstractActionComponent> C onMouseOver(final EventHandler handler) {
		onEvent(JsEvent.mouseover, handler);

		return (C) this;
	}

	public <C extends AbstractActionComponent> C onMouseUp(final EventHandler handler) {
		onEvent(JsEvent.mouseup, handler);

		return (C) this;
	}

	public <C extends AbstractActionComponent> C onMouseWheel(final EventHandler handler) {
		onEvent(JsEvent.mousewheel, handler);

		return (C) this;
	}
}
