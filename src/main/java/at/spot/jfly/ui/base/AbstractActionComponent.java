package at.spot.jfly.ui.base;

import at.spot.jfly.style.GlyphIcon;

public class AbstractActionComponent extends AbstractTextComponent {
	private GlyphIcon icon;

	public AbstractActionComponent(String text) {
		super(text);
	}

	public <C extends AbstractActionComponent> C glyphIcon(final GlyphIcon icon) {
		this.icon = icon;
		updateClientComponent();
		return (C) this;
	}

	public GlyphIcon glyphIcon() {
		return icon;
	}

}
