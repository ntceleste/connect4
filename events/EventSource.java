package connect4.events;

/**
 * This class is used as a generic source for events
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public class EventSource<EventDataType extends EventData> {

	//An array of event listeners that have been attached to this event
	private IEventListener<EventDataType>[] _eventListeners;
	
	/**
	 * Creates a new generic EventSource
	 */
	@SuppressWarnings("unchecked")
	public EventSource() {
		_eventListeners = new IEventListener[0];
	}
	
	/**
	 * Iteratively invokes all listeners, passing to each the given EventData argument
	 * @param data The EventData to be passed along to each listener
	 */
	public void notifyListeners(EventDataType data) {
		for (int i = 0; i < _eventListeners.length; i++) {
			_eventListeners[i].handleNotification(data);
		}
	}
	
	/**
	 * Adds a new Listener to be attached to this EventSource
	 * @param listener The listener to be added
	 * @param atBeginning If true, the new listener will become the first listener to hear about the event (can be overrided by any other listener taking the spot at the beginning in the future)
	 */
	public void addListener(IEventListener<EventDataType> listener, boolean atBeginning) {
		@SuppressWarnings("unchecked")
		IEventListener<EventDataType>[] newArray = new IEventListener[_eventListeners.length + 1];
		
		int currentIndex = 0;
		
		if (atBeginning) {
			newArray[0] = listener;
			currentIndex++;
		}
		else  {
			newArray[newArray.length - 1] = listener;
		}
		
		for (int i = 0; i < _eventListeners.length; i++) {
			newArray[currentIndex] = _eventListeners[i];
			currentIndex++;
		}
		
		_eventListeners = newArray;
	}
	
	/**
	 * Adds a new Listener to be attached to this EventSource. Assumes atBeginning to equal false
	 * @param listener The listener to be added
	 */
	public void addListener(IEventListener<EventDataType> listener) {
		addListener(listener, false);
	}

}