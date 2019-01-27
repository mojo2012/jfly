package io.spotnext.jfly.event;

import io.spotnext.jfly.util.DynamicEnum;

public interface Events {

	public interface EventType extends io.spotnext.jfly.util.Enum {
	}

	public static class EventEnum extends DynamicEnum implements EventType {
		protected EventEnum(String internalName) {
			super(internalName);
		}
	}

	public static class JsEvent extends EventEnum implements EventType {
		private JsEvent(String internalName) {
			super(internalName);
		}

		public static JsEvent Click = new JsEvent("click");
		public static JsEvent Hover = new JsEvent("hover");
		public static JsEvent Input = new JsEvent("input");
		public static JsEvent MouseDown = new JsEvent("mousedown");
		public static JsEvent MouseUp = new JsEvent("mouseup");
		public static JsEvent Focus = new JsEvent("focus");
		public static JsEvent Blur = new JsEvent("blur");
		public static JsEvent KeyDown = new JsEvent("keydown");
		public static JsEvent Change = new JsEvent("change");
		public static JsEvent DoubleClick = new JsEvent("dblclick");
		public static JsEvent MouseMove = new JsEvent("mousemove");
		public static JsEvent MouseOver = new JsEvent("mouseover");
		public static JsEvent MouseOut = new JsEvent("mouseout");
		public static JsEvent MouseWheel = new JsEvent("mousewheel");
		public static JsEvent KeyUp = new JsEvent("keyup");
		public static JsEvent KeyPress = new JsEvent("keypress");
		public static JsEvent TextInput = new JsEvent("textInput");
		public static JsEvent TouchStart = new JsEvent("touchstart");
		public static JsEvent TouchMove = new JsEvent("touchmove");
		public static JsEvent TouchEnd = new JsEvent("touchend");
		public static JsEvent TouchCancel = new JsEvent("touchcancel");
		public static JsEvent Resize = new JsEvent("resize");
		public static JsEvent Scroll = new JsEvent("scroll");
		public static JsEvent Zoom = new JsEvent("zoom");
		public static JsEvent Select = new JsEvent("select");
		public static JsEvent Submit = new JsEvent("submit");
		public static JsEvent Reset = new JsEvent("reset");

		// window history navigation events
		public static JsEvent PopState = new JsEvent("popstate");
		public static JsEvent Unload = new JsEvent("unload");
		public static JsEvent BeforeUnload = new JsEvent("onbeforeunload");
		public static JsEvent Load = new JsEvent("load");
	}

	public static class GenericEvent extends EventEnum implements EventType {
		private GenericEvent(String internalName) {
			super(internalName);
		}

		public static JsEvent StateChanged = new JsEvent("stateChanged");
	}
}
