package io.spotnext.jfly.ui.display;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;

public class Image extends AbstractComponent {
	private String sourceUrl;
	private double aspectRatio = 1;
	private Integer height;
	private Integer width;
	private Integer minHeight;
	private Integer minWidth;
	private Integer maxHeight;
	private Integer maxWidth;

	public Image(final ComponentHandler handler) {
		this(handler, "");
	}

	public Image(final ComponentHandler handler, String sourceUrl) {
		super(handler);
		this.sourceUrl = sourceUrl;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
		updateClientComponent();
	}

	public double getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(double aspectRatio) {
		this.aspectRatio = aspectRatio;
		updateClientComponent();
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
		updateClientComponent();
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
		updateClientComponent();
	}

	public Integer getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(Integer minHeight) {
		this.minHeight = minHeight;
		updateClientComponent();
	}

	public Integer getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
		updateClientComponent();
	}

	public Integer getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(Integer maxHeight) {
		this.maxHeight = maxHeight;
		updateClientComponent();
	}

	public Integer getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
		updateClientComponent();
	}

}
