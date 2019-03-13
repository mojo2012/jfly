jfly = {
	constants : {
		COMPONENT_STATES: "componentStates",
		ATTRIBUTE_UUID: "uuid",
	}
};

jfly.connection = {};

// When the connection is open, send some data to the server
jfly.websockethandler = {
	// initializes the handler
	init: function() {
		jfly.websockethandler.connection = new WebSocket('ws://' + window.location.hostname + ":" + window.location.port + '/com');
		jfly.websockethandler.connection.onmessage = jfly.websockethandler.onmessage;
		jfly.websockethandler.connection.onopen = jfly.websockethandler.onopen;
		jfly.websockethandler.connection.onerror = jfly.websockethandler.onerror;
	},
	
	keepAlive: function() {
		console.log("Sending websocket keep alive");
		jfly.websockethandler.send({ type: "keepAlive" });
		
		setTimeout(jfly.websockethandler.keepAlive, 600000);
	},
	
	// send a message to the server
	send(message) {
		let jsonCookieString = atob(jfly.getCookie("jfly"));
		let jsonCookie = JSON.parse(jsonCookieString);
		message.sessionId = jsonCookie.value;
		message.viewId = viewId;
		message.url = window.location.pathname;
//		message.locale = navigator.language;
		
		let msg = jfly.toString(message);
		
		// check the connection status and re-initialize connection in case it's disconnected
		if (jfly.websockethandler.connection.readyState == 3) {
			jfly.websockethandler.init();
		}
		
		try {
			jfly.websockethandler.connection.send(msg);
		} catch (e) {
			console.log(e);
			jfly.uicontroller.showExceptionDialog({ description: "Sorry, the service seems to be interrupted." });
		}
	},
	
	// event handling
	onopen: function (event) {
		console.debug('WebSocket onopen ' + event);
		
		if (applicationState) {
			jfly.initVue(applicationState);
		} else {
			// only request initial component states if vue has not yet been initialized
			jfly.callAsync(function() {
				jfly.websockethandler.send({ "type": 'initialStateRequest' });
			});
		}
		
		jfly.websockethandler.keepAlive();
	},
	
	onclose: function (event) {
		console.debug('WebSocket onclose ' + event);
	},
	
	// Log errors
	onerror: function (event) {
		console.debug('WebSocket onerror ' + event);
	},
	
	// Log messages from the server
	onmessage: function (event) {
		console.debug('WebSocket onmessage');
		let message = JSON.parse(event.data);
		
		if (message.type == "keepAlive") {
			// ignore pong
		} else if (message.type == "initialStateUpdate") {
			jfly.callAsync(function() {
				jfly.initVue(message);
			});
		} else if (message.type == "componentManipulation") {
			let component = jfly.findComponent(message.componentUuid);
			component[message.method].apply(component, message.parameters);
		} else if (message.type == "functionCall") {
			let func = window[message.object][message.functionCall];
			func.apply(func, message.parameters);
		} else if (message.type == "componentStateUpdate") {
//			jfly.uicontroller.componentStates[message.componentUuid] = message.componentState;
			jQuery.extend(jfly.uicontroller.componentStates[message.componentUuid], message.componentState);
		} else if (message.type == "notification") {
			// show notification
		} else if (message.type == "stateRequest") {
			// send component state back
		} else if (message.type == "exception") {
			jfly.uicontroller.showExceptionDialog(message);
		}
	},
}

jfly.scrollTo = function(y) {
	
}

// converts objects to json strings
jfly.toString = function(object) {
	return JSON.stringify(object);
},

jfly.callAsync = function(func) {
	setTimeout(func, 0);
}

jfly.reloadApp = function() {
	location.reload();
};

jfly.getCookie = function(cookieId, subKey) {
	let cookies = jfly.getCookies();
	
	if (subKey) {
		return cookies[cookieId][subKey];
	} else {
		return cookies[cookieId]
	}
}

jfly.getCookies = function(){
	let cookies = {};
	for (c of document.cookie.split(";")) {
		let obj = c.split("=");
		
		let key = obj[0].trim();
		let value = obj[1];
		
		try {
			value = JSON.parse(JSON.parse(value));
	    } catch (e) {
	    }
	    cookies[key] = value;
	}
	
	return cookies;
}

jfly.findComponent = function(componentUuid) {
	let component = $("[" + jfly.constants.ATTRIBUTE_UUID + "='" + componentUuid + "']");
	
	return component;
};

jfly.removeComponent = function(componentUuid) {
	jfly.findComponent(componentUuid).remove();
};

jfly.removeChildComponent = function(containerUuid, childUuid) {
	jfly.findComponent(containerUuid).find("[" + jfly.constants.ATTRIBUTE_UUID + "='" + uuid + "']").remove();
};

jfly.addChildComponent = function(containerUuid, childHtml, childContext) {
	jfly.uicontroller.addChildComponent(containerUuid, childHtml, childContext);
};

jfly.replaceComponent = function(componentUuid, component) {
	let elem = $(component);
	
	jfly.findComponent(componentUuid).replaceWith(elem);
	jfly.setupEventHandling(jfly.findComponent(componentUuid));
};

jfly.hideComponent = function(componentUuid) {
	jfly.findComponent(componentUuid).addClass("hidden");
};

jfly.showComponent = function(componentUuid) {
	jfly.findComponent(componentUuid).removeClass("hidden");
};

jfly.registerEvent = function(componentUuid, event) {
	jfly.findComponent(componentUuid).on(event, function(event){
		jfly.handleEvent(this, event);
	});
};

jfly.unregisterEvent = function(componentUuid, event) {
	jfly.findComponent(componentUuid).unbind(event);
};

jfly.findElementById = function(id) {
	return $("#" + id);
};

jfly.replaceElementWithId = function(id, element) {
	jfly.findElementById(id).replaceWith(element);
};

jfly.debounce = function(func, wait, immediate) {
	let timeout;
	
	return function() {
		let context = this, args = arguments;
		let later = function() {
			timeout = null;
			if (!immediate) func.apply(context, args);
		};
		
		let callNow = immediate && !timeout;
		clearTimeout(timeout);
		timeout = setTimeout(later, wait);
		
		if (callNow) func.apply(context, args);
	};
};

jfly.init = function() {
	jfly.websockethandler.init();
	
	// TODO bug in vue? hashchange is not working properly
	window.onhashchange = function(event) {
		jfly.uicontroller.handleEvent("hashchange", null, event, jfly.getCurrentUrlLocation());
	};
	
	window.onbeforeunload = function(event) {
//		window.history.pushState({page: 1}, "", "");
	}
};

jfly.getCurrentUrlLocation = function() {
	return { currentUrl: window.location.pathname, currentUrlHash: window.location.hash };
}

jfly.initVue = function(initMessage) {
	jfly.uicontroller = new Vue({
		el: 'v-app',
		components: {
		},
		methods: {
			// this weird little debounce functionality prevents event flooding
			handleEvent: function(event, componentUuid, eventData, additionalData) {
				let message = {};
				
				// special handling for vuetify components that don't emit native JS events
				// eg. dropdown box which only provides the selected menuItem as eventData
				if (typeof(eventData) === "object" && eventData.target) {
					// events cannot be serialized, so we need this hack
					message.domEventData = { ...this.serializeEvent(eventData), ...additionalData };
				} else {
					if (typeof(eventData) === "string") {
						eventData = { "data" : eventData };						
					}
					
					message.domEventData = { "eventName": event, "data": eventData };
				}
				
				message.type = "event";
				message.eventType = event;
				message.componentUuid = componentUuid;

				// add whole component state to the event payload
				message.componentState = this.componentStates[componentUuid];
				
				jfly.websockethandler.send(message);
			},
			
			serializeEvent: function(e) {
				let ret = null;
				
				if (e) {
					ret = {
						eventName: e.type,
						altKey: e.altKey,
						bubbles: e.bubbles,
						button: e.button,
						buttons: e.buttons,
						cancelBubble: e.cancelBubble,
						cancelable: e.cancelable,
						clientX: e.clientX,
						clientY: e.clientY,
						composed: e.composed,
						ctrlKey: e.ctrlKey,
						currentTarget: e.currentTarget ? e.currentTarget.outerHTML : null,
						defaultPrevented: e.defaultPrevented,
						detail: e.detail,
						eventPhase: e.eventPhase,
						fromElement: e.fromElement ? e.fromElement.outerHTML : null,
						isTrusted: e.isTrusted,
						layerX: e.layerX,
						layerY: e.layerY,
						metaKey: e.metaKey,
						movementX: e.movementX,
						movementY: e.movementY,
						offsetX: e.offsetX,
						offsetY: e.offsetY,
						pageX: e.pageX,
						pageY: e.pageY,
						//path: pathToSelector(e.path && e.path.length ? e.path[0] : null),
						relatedTarget: e.relatedTarget ? e.relatedTarget.outerHTML : null,
						returnValue: e.returnValue,
						screenX: e.screenX,
						screenY: e.screenY,
						shiftKey: e.shiftKey,
						sourceCapabilities: e.sourceCapabilities ? e.sourceCapabilities.toString() : null,
						target: e.target ? e.target.outerHTML : null,
						timeStamp: e.timeStamp,
						toElement: e.toElement ? e.toElement.outerHTML : null,
						type: e.type,
						view: e.view ? e.view.toString() : null,
						which: e.which,
						x: e.x,
						y: e.y,
						data: e.data
					}
				}
				
				return ret;
			},
			
			getComponentStateProperty(componentUuid, propertyName) {
				jfly.uicontroller.data[property]
			},
			
			addChildComponent: function(containerUuid, childHtml, childContext) {
				let childUuid = childContext.uuid;

				this.componentStates[childContext.uuid] = childContext;
				
				// "this" can't be referenced within the data function,
				// so we have to create a proxy for it
				let parent = this;
				let Component = Vue.extend({
					parent: jfly.uicontroller,
					template: childHtml,
					data: function() {
						// inject the parent states
						return parent.$data
					},
					methods: {
						// this is necessary, as the child components can not access the parent's methods
						localize: parent.localize,
					}
					// TOOD: also map filters and other stuff?
				});
					 
				let component = new Component().$mount();
				jfly.findComponent(containerUuid).append(component.$el);
			},
			
			showExceptionDialog: function(message) {
				this.exception.message = message.description;
				this.exception.visible = true;
			},
			
			localize: function(value) {
				let ret = value;
				
				if (value && value.defaultValue) {
					ret = value.defaultValue;
				} else if (value && value.values) {
					let globalState = this.globalState;
					
					ret = value.values[globalState.currentLocale.code];
					
					if (!ret) {
						ret = "<no localization found>";
					}
				}
				
				return ret;
			}
		},
		computed: {
		},
		data: {
			componentStates: initMessage.componentStates,
			globalState: initMessage.globalState,
			exception: {
				visible: false,
				message: ""
			}
		},
		filters: {
			capitalize: function (value) {
				if (!value) return '';
				value = value.toString();
				return value.charAt(0).toUpperCase() + value.slice(1)
			},
		},
		beforeCreate: function() {
			console.debug("Vue beforeCreate");
		},
		created: function() {
			console.debug("Vue created");
		},
		beforeMount: function() {
			console.debug("Vue beforeMount");
			
			let vue = this;
			
			// register events
			for (uuid in this.componentStates) {
				let componentState = this.componentStates[uuid];
				let events = componentState.registeredEvents;
				
				if (events) {
					componentState.eventHandlers = {};

					events.forEach(function(event) {
						// this is an ugly hack to put the loop's "current" component uuid into the closure.
						// if not wrapped into a anonymous function, the last uuid of the loop will be used for all event handlers
						(function(){
							let compUuid = uuid;
							
							let handler = function(eventData) {
								vue.handleEvent(event, compUuid, eventData);
							};
							
							// debounce those event types to prevent too much traffic 
							if (event == "input" || event == "change") {
								handler = jfly.debounce(handler, 400);
							}
							
							componentState.eventHandlers[event] = handler;
						})();
					});
				}
			}
			
			// search for all components that want one or more of its properties being watched for changes
			$("[watch-for-state-change]").each(function(i) {
				let propertiesToWatch = eval($(this).attr("watch-for-state-change"));
				let componentUuid = $(this).attr(jfly.constants.ATTRIBUTE_UUID);
				
				propertiesToWatch.forEach(function(property) {
					let selector = jfly.constants.COMPONENT_STATES + "." + componentUuid + "." + property;
					
					(function(){
						vue.$watch(selector, jfly.debounce(function(newValue, oldValue) {
							// have to create the object like this, otherwise property would be "optimized out" ...
							let eventData = {};
							eventData[property] = newValue;
							
							vue.handleEvent("stateChanged", componentUuid, eventData);
						}, 400));
					})();
				});
				
			});
		},
		mounted: function() {
			console.debug("Vue mounted");
			
			let body = $("body")[0];
			let bodyComponentId = body.attributes["uuid"].value;
			
			this.handleEvent("load", bodyComponentId, jfly.getCurrentUrlLocation(), null);
		},
	});
}

/*
 * Init jfly when page is ready
 */
// Zepto(function($) {
$( document ).ready(function() {
	jfly.init();
});

