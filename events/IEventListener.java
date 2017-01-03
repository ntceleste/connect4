package connect4.events;

/**
 * This interface is used so that classes can implement this and have listener methods for EventSources
 * @author Nate Celeste NTC14, Noah Crowley NWC17
 */
public interface IEventListener<EventDataType extends EventData> {

	/**
	 * To be implemented to handle a notification from an EventSource
	 * @param data The EventData that was given when the EventSource was fired
	 */
    public void handleNotification(EventDataType data);

}
