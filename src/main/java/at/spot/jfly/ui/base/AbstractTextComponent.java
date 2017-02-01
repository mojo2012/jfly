package at.spot.jfly.ui.base;

public abstract class AbstractTextComponent extends AbstractComponent {
	private String text;

	public AbstractTextComponent(final String text) {
		text(text);
	}

	public <C extends AbstractTextComponent> C text(final String text) {
		this.text = text;
		updateClientComponent();
		return (C) this;
	}

	public String text() {
		return this.text;
	}
}
