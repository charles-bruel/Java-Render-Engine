package net.chazzvader.core.generic.engine.event.type;

import java.util.ArrayList;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.event.ActionType;
import net.chazzvader.core.generic.engine.event.EventManager;
import net.chazzvader.core.generic.engine.event.IEventDispatcher;

/**
 * Class containing all event dispatchers for all core, default events
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class EventDispatchers {

	private EventDispatchers() {
	}

	/**
	 * Event dispatcher for mouse button and key events. Extra special functionality
	 * because the eventdata can be for multiple events
	 * 
	 * @see EventDataIDAndAction
	 */
	public static final IEventDispatcher ID_AND_ACTION_EVENT_DISPATCHER = new IEventDispatcher() {

		/**
		 * Measures, in milliseconds, how long between clicks could be considered a double/triple/etc click.
		 */
		public static final int CLICK_INTERVAL = 500;
		
		private long timeAtLastButtonPress = 0;
		private int clickCount = 1;
		
		@Override
		public boolean dispatch(EventManager eventManager, EventData eventData, Object handler) {
			if (eventData.EVENT_TYPE == EventTypes.EVENT_KEY) {
				if (handler instanceof IEventHandlerKey && eventData instanceof EventDataIDAndAction) {
					IEventHandlerKey keyHandler = (IEventHandlerKey) handler;
					EventDataIDAndAction keyEventData = (EventDataIDAndAction) eventData;
					if (keyEventData.type == ActionType.PRESSED) {
						return keyHandler.keyPressed(keyEventData.id, keyEventData.modifers, keyEventData.ctrl, keyEventData.shift, keyEventData.alt, keyEventData.os, keyEventData.capLock, keyEventData.numLock);
					} else if (keyEventData.type == ActionType.RELEASED) {
						return keyHandler.keyReleased(keyEventData.id, keyEventData.modifers, keyEventData.ctrl, keyEventData.shift, keyEventData.alt, keyEventData.os, keyEventData.capLock, keyEventData.numLock);
					}
				} else {
					Logging.log("One or more invalid types passed", "Event System", LoggingLevel.ERR);
				}
			} else if (eventData.EVENT_TYPE == EventTypes.EVENT_MOUSE_BUTTON) {
				if (handler instanceof IEventHandlerMouseButton && eventData instanceof EventDataIDAndAction) {
					IEventHandlerMouseButton mouseHandler = (IEventHandlerMouseButton) handler;
					EventDataIDAndAction mouseEventData = (EventDataIDAndAction) eventData;
					if (mouseEventData.type == ActionType.PRESSED) {
						if(System.nanoTime()-timeAtLastButtonPress <= CLICK_INTERVAL * 1000000) {
							clickCount ++;
						} else {
							clickCount = 1;
						}
						timeAtLastButtonPress = System.nanoTime();
						return mouseHandler.mouseButtonPressed(mouseEventData.id, mouseEventData.modifers, mouseEventData.ctrl, mouseEventData.shift, mouseEventData.alt, mouseEventData.os, mouseEventData.capLock, mouseEventData.numLock, clickCount);
					} else if (mouseEventData.type == ActionType.RELEASED) {
						return mouseHandler.mouseButtonReleased(mouseEventData.id, mouseEventData.modifers, mouseEventData.ctrl, mouseEventData.shift, mouseEventData.alt, mouseEventData.os, mouseEventData.capLock, mouseEventData.numLock);
					}
				} else {
					Logging.log("One or more invalid types passed", "Event System", LoggingLevel.ERR);
				}
			} else {
				Logging.log("Invalid event type", "Event System", LoggingLevel.ERR);
			}
			return false;
		}
	};

	/**
	 * Event dispatcher for mouse moved events
	 * 
	 * @see EventDataMouseMoved
	 */
	public static final IEventDispatcher MOUSE_MOVED_EVENT_DISPATCHER = new IEventDispatcher() {

		private int lastX, lastY;
		
		private boolean dispatchInd(EventManager eventManager, EventData eventData, Object handler) {
			if (eventData.EVENT_TYPE == EventTypes.EVENT_MOUSE_MOVED) {
				if (handler instanceof IEventHandlerMouseMoved && eventData instanceof EventDataMouseMoved) {
					IEventHandlerMouseMoved mouseMovedHandler = (IEventHandlerMouseMoved) handler;
					EventDataMouseMoved mouseMovedData = (EventDataMouseMoved) eventData;
					int iXpos = mouseMovedData.xpos;
					int iYpos = mouseMovedData.ypos;
					int xOffset = iXpos - lastX;
					int yOffset = -(iYpos - lastY);
					return mouseMovedHandler.updateMouseAbsolutePosition(iXpos, iYpos)
							|| mouseMovedHandler.updateMouseOffset(xOffset, yOffset);
				} else {
					Logging.log("One or more invalid types passed", "Event System", LoggingLevel.ERR);
				}
			} else {
				Logging.log("Invalid event type", "Event System", LoggingLevel.ERR);
			}
			return false;
		}



		@Override
		public boolean dispatch(EventManager eventManager, EventData eventData, Object handler) {
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Object>> eventHandlers = (ArrayList<ArrayList<Object>>) handler;
			for(int i = 0;i < eventHandlers.size();i ++) {
				ArrayList<Object> handlers = eventHandlers.get(i);
				for(int j = 0;j < handlers.size();j ++) {
					boolean retTrue = dispatchInd(null, eventData, handlers.get(j));
					if(retTrue) return true;
				}
			}
			EventDataMouseMoved data = (EventDataMouseMoved) eventData;
			lastX = data.xpos;
			lastY = data.ypos;
			return false;
		}
	};

	/**
	 * Event dispatcher for window resized events
	 * 
	 * @see EventDataWindowResized
	 */
	public static final IEventDispatcher WINDOW_RESIZED_EVENT_DISPATCHER = new IEventDispatcher() {

		@Override
		public boolean dispatch(EventManager eventManager, EventData eventData, Object handler) {
			if (eventData.EVENT_TYPE == EventTypes.EVENT_WINDOW_RESIZED) {
				if (handler instanceof IEventHandlerWindowResized && eventData instanceof EventDataWindowResized) {
					IEventHandlerWindowResized windowHandler = (IEventHandlerWindowResized) handler;
					EventDataWindowResized windowData = (EventDataWindowResized) eventData;
					return windowHandler.windowResized(windowData.width, windowData.height);
				} else {
					Logging.log("One or more invalid types passed", "Event System", LoggingLevel.ERR);
				}
			} else {
				Logging.log("Invalid event type", "Event System", LoggingLevel.ERR);
			}
			return false;
		}
	};
	
	/**
	 * Event dispatcher for scroll wheel events
	 * 
	 * @see EventDataScroll
	 */
	public static final IEventDispatcher SCROLL_EVENT_DISPATCHER = new IEventDispatcher() {

		@Override
		public boolean dispatch(EventManager eventManager, EventData eventData, Object handler) {
			if (eventData.EVENT_TYPE == EventTypes.EVENT_SCROLL) {
				if (handler instanceof IEventHandlerScroll && eventData instanceof EventDataScroll) {
					IEventHandlerScroll scrollHandler = (IEventHandlerScroll) handler;
					EventDataScroll scrollData = (EventDataScroll) eventData;
					return scrollHandler.scrollWheel(scrollData.xoffset, scrollData.yoffset);
				} else {
					Logging.log("One or more invalid types passed", "Event System", LoggingLevel.ERR);
				}
			} else {
				Logging.log("Invalid event type", "Event System", LoggingLevel.ERR);
			}
			return false;
		}
	};

}
