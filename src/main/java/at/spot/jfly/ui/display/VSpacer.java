package at.spot.jfly.ui.display;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractTextComponent;
import io.gsonfire.annotations.ExposeMethodResult;

public class VSpacer extends AbstractTextComponent {

	public VSpacer(ComponentHandler handler) {
		super(handler, "");
	}

	@ExposeMethodResult("isSpacer")
	public boolean isSpacer() {
		return true;
	}
}
