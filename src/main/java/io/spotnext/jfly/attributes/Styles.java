package io.spotnext.jfly.attributes;

import io.spotnext.jfly.util.DynamicEnum;

public interface Styles {

	public interface Style extends io.spotnext.jfly.util.Enum {
	}

	public static class Color extends DynamicEnum implements Style {
		protected Color(String internalName) {
			super(internalName);
		}

		public static Color RED = new Color("red");
		public static Color PINK = new Color("pink");
		public static Color PURPLE = new Color("purple");
		public static Color DEEP_PURPLE = new Color("deep-purple");
		public static Color INDIGO = new Color("indigo");
		public static Color BLUE = new Color("blue");
		public static Color LIGHT_BLUE = new Color("light-blue");
		public static Color CYAN = new Color("cyan");
		public static Color TEAL = new Color("teal");
		public static Color GREEN = new Color("green");
		public static Color LIGHT_GREEN = new Color("light-green");
		public static Color LIME = new Color("lime");
		public static Color YELLOW = new Color("yellow");
		public static Color AMBER = new Color("amber");
		public static Color ORANGE = new Color("orange");
		public static Color DEEP_ORANGE = new Color("deep-orange");
		public static Color BROWN = new Color("brown");
		public static Color BLUE_GREY = new Color("blue-grey");
		public static Color GREY = new Color("grey");
		public static Color BLACK = new Color("black");
		public static Color WHITE = new Color("white");
		public static Color TRANSPARENT = new Color("transparent");
	}

	public static class ColorModifier extends DynamicEnum implements Style {
		protected ColorModifier(String internalName) {
			super(internalName);
		}

		public static Color LIGHTEN_1 = new Color("lighten-1");
		public static Color LIGHTEN_2 = new Color("lighten-1");
		public static Color LIGHTEN_3 = new Color("lighten-3");
		public static Color LIGHTEN_4 = new Color("lighten-4");
		public static Color LIGHTEN_5 = new Color("lighten-5");

		public static Color DARKEN_1 = new Color("darken-1");
		public static Color DARKEN_2 = new Color("darken-1");
		public static Color DARKEN_3 = new Color("darken-3");
		public static Color DARKEN_4 = new Color("darken-4");

		public static Color ACCENT_1 = new Color("accent-1");
		public static Color ACCENT_2 = new Color("accent-2");
		public static Color ACCENT_3 = new Color("accent-3");
		public static Color ACCENT_4 = new Color("accent-4");
	}

	// public static class BadgeStyle extends DynamicEnum implements Style {
	// protected BadgeStyle(String internalName) {
	// super(internalName);
	// }
	//
	// public static Color Black = new Color("w3-black");
	// public static Color White = new Color("w3-white");
	// public static Color Green = new Color("w3-green");
	// public static Color Red = new Color("w3-green");
	// public static Color Blue = new Color("w3-red");
	// public static Color Turquoise = new Color("w3-turquoise");
	// public static Color LightGreen = new Color("w3-metro-light-green");
	// }
}
