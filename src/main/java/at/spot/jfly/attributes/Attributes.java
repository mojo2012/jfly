package at.spot.jfly.attributes;

import at.spot.jfly.util.DynamicEnum;

public interface Attributes {

	public interface Attribute extends at.spot.jfly.util.Enum {
	}

	public static class GenericAttribute extends DynamicEnum implements Attribute {
		private GenericAttribute(String internalName) {
			super(internalName);
		}

		public static GenericAttribute RED = new GenericAttribute("red");
	}

	public static class ButtonType extends DynamicEnum implements Attribute {
		protected ButtonType(String internalName) {
			super(internalName);
		}

		public static TextFieldType Success = new TextFieldType("success");
		public static TextFieldType Info = new TextFieldType("info");
		public static TextFieldType Warning = new TextFieldType("warning");
		public static TextFieldType Error = new TextFieldType("error");
	}

	public static class TextFieldType extends DynamicEnum implements Attribute {
		protected TextFieldType(String internalName) {
			super(internalName);
		}

		public static TextFieldType Solo = new TextFieldType("solo");
		public static TextFieldType SoloInverted = new TextFieldType("solo-inverted");

	}

	public static class HorizontalOrientation extends DynamicEnum implements Attribute {
		protected HorizontalOrientation(String internalName) {
			super(internalName);
		}

		public static HorizontalOrientation Left = new HorizontalOrientation("left");
		public static HorizontalOrientation Right = new HorizontalOrientation("right");
	}

	public static class GridAlignment extends DynamicEnum implements Attribute {
		protected GridAlignment(String internalName) {
			super(internalName);
		}

		public static GridAlignment BaseLine = new GridAlignment("align-baseline");
		public static GridAlignment Center = new GridAlignment("align-center");
		public static GridAlignment ContentCenter = new GridAlignment("align-content-center");
		public static GridAlignment ContentEnd = new GridAlignment("align-content-end");
		public static GridAlignment ContentSpaceAround = new GridAlignment("align-content-space-around");
		public static GridAlignment ContentSpaceBetween = new GridAlignment("align-content-space-between");
		public static GridAlignment ContentStart = new GridAlignment("align-content-start");
		public static GridAlignment End = new GridAlignment("align-end");
		public static GridAlignment Start = new GridAlignment("align-start");

	}

	public static class GridLayoutSize extends DynamicEnum implements Attribute {
		protected GridLayoutSize(String internalName) {
			super(internalName);
		}

		public static GridLayoutSize XS12 = new GridLayoutSize("xs12");
		public static GridLayoutSize XS6 = new GridLayoutSize("xs5");
		public static GridLayoutSize XS43 = new GridLayoutSize("xs4");
		public static GridLayoutSize XS3 = new GridLayoutSize("xs3");
		public static GridLayoutSize XS2 = new GridLayoutSize("xs2");
		public static GridLayoutSize XS1 = new GridLayoutSize("xs1");
	}

	public static class GridBehavior extends DynamicEnum implements Attribute {
		protected GridBehavior(String internalName) {
			super(internalName);
		}

		public static GridBehavior Fluid = new GridBehavior("fluid");
		public static GridBehavior Wrap = new GridBehavior("wrap");
		public static GridBehavior FillHeight = new GridBehavior("fill-height");
		public static GridBehavior JustifyCenter = new GridBehavior("justify-center");
		public static GridBehavior JustifyEnd = new GridBehavior("justify-end");
		public static GridBehavior JustifyStart = new GridBehavior("justify-start");
		public static GridBehavior JustifySpaceAround = new GridBehavior("justify-space-around");
		public static GridBehavior JustifySpaceBetween = new GridBehavior("justify-space-between");
	}

}
