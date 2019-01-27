package io.spotnext.jfly.event;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.spotnext.jfly.event.Events.EventType;
import io.spotnext.jfly.ui.base.Component;

public class DomEvent extends ComponentEvent {
	// JS event properties
	// private EventType eventName;
	private boolean altKey;
	private boolean bubbles;
	private int button;
	private int buttons;
	private boolean cancelBubble;
	private boolean cancelable;
	private int clientX;
	private int clientY;
	private boolean composed;
	private boolean ctrlKey;
	private String currentTarget;
	private boolean defaultPrevented;
	private int detail;
	private int eventPhase;
	private Object fromElement;

	@JsonProperty(value = "isTrusted")
	private boolean trusted;
	private int layerX;
	private int layerY;
	private boolean metaKey;
	private int movementX;
	private int movementY;
	private int offsetX;
	private int offsetY;
	private int pageX;
	private int pageY;
	private String path;
	private Object relatedTarget;
	private Object returnValue;
	private int screenX;
	private int screenY;
	private boolean shiftKey;
	private Object sourceCapabilities;
	private String target;
	private int timeStamp;
	private Object toElement;
	private String type;
	private String view;
	private int which;
	private int x;
	private int y;
	private final Map<String, Object> data = new HashMap<>();

	// custom properties

	private Component source;

	public Component getSource() {
		return this.source;
	}

	public EventType getEventType() {
		return eventType;
	}

	public boolean isAltKey() {
		return altKey;
	}

	public void setAltKey(boolean altKey) {
		this.altKey = altKey;
	}

	public boolean isBubbles() {
		return bubbles;
	}

	public void setBubbles(boolean bubbles) {
		this.bubbles = bubbles;
	}

	public boolean isCancelBubble() {
		return cancelBubble;
	}

	public void setCancelBubble(boolean cancelBubble) {
		this.cancelBubble = cancelBubble;
	}

	public boolean isCancelable() {
		return cancelable;
	}

	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}

	public boolean isComposed() {
		return composed;
	}

	public void setComposed(boolean composed) {
		this.composed = composed;
	}

	public boolean isCtrlKey() {
		return ctrlKey;
	}

	public void setCtrlKey(boolean ctrlKey) {
		this.ctrlKey = ctrlKey;
	}

	public String getCurrentTarget() {
		return currentTarget;
	}

	public void setCurrentTarget(String currentTarget) {
		this.currentTarget = currentTarget;
	}

	public boolean isDefaultPrevented() {
		return defaultPrevented;
	}

	public void setDefaultPrevented(boolean defaultPrevented) {
		this.defaultPrevented = defaultPrevented;
	}

	public int getDetail() {
		return detail;
	}

	public void setDetail(int detail) {
		this.detail = detail;
	}

	public int getEventPhase() {
		return eventPhase;
	}

	public void setEventPhase(int eventPhase) {
		this.eventPhase = eventPhase;
	}

	public Object getFromElement() {
		return fromElement;
	}

	public void setFromElement(Object fromElement) {
		this.fromElement = fromElement;
	}

	public boolean isTrusted() {
		return trusted;
	}

	public void setTrusted(boolean isTrusted) {
		this.trusted = isTrusted;
	}

	public int getLayerX() {
		return layerX;
	}

	public void setLayerX(int layerX) {
		this.layerX = layerX;
	}

	public int getLayerY() {
		return layerY;
	}

	public void setLayerY(int layerY) {
		this.layerY = layerY;
	}

	public boolean isMetaKey() {
		return metaKey;
	}

	public void setMetaKey(boolean metaKey) {
		this.metaKey = metaKey;
	}

	public int getPageX() {
		return pageX;
	}

	public void setPageX(int pageX) {
		this.pageX = pageX;
	}

	public int getPageY() {
		return pageY;
	}

	public void setPageY(int pageY) {
		this.pageY = pageY;
	}

	public Object getRelatedTarget() {
		return relatedTarget;
	}

	public void setRelatedTarget(Object relatedTarget) {
		this.relatedTarget = relatedTarget;
	}

	public boolean isShiftKey() {
		return shiftKey;
	}

	public void setShiftKey(boolean shiftKey) {
		this.shiftKey = shiftKey;
	}

	public Object getSourceCapabilities() {
		return sourceCapabilities;
	}

	public void setSourceCapabilities(Object sourceCapabilities) {
		this.sourceCapabilities = sourceCapabilities;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Object getToElement() {
		return toElement;
	}

	public void setToElement(Object toElement) {
		this.toElement = toElement;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public int getWhich() {
		return which;
	}

	public void setWhich(int which) {
		this.which = which;
	}

	@JsonAnyGetter
	public Map<String, Object> getData() {
		return data;
	}

	@JsonAnySetter
	public void addData(String key, Object value) {
		this.data.put(key, value);
	}

	public int getButton() {
		return button;
	}

	public void setButton(int button) {
		this.button = button;
	}

	public int getButtons() {
		return buttons;
	}

	public void setButtons(int buttons) {
		this.buttons = buttons;
	}

	public int getClientX() {
		return clientX;
	}

	public void setClientX(int clientX) {
		this.clientX = clientX;
	}

	public int getClientY() {
		return clientY;
	}

	public void setClientY(int clientY) {
		this.clientY = clientY;
	}

	public int getMovementX() {
		return movementX;
	}

	public void setMovementX(int movementX) {
		this.movementX = movementX;
	}

	public int getMovementY() {
		return movementY;
	}

	public void setMovementY(int movementY) {
		this.movementY = movementY;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public void setSource(Component source) {
		this.source = source;
	}
}
