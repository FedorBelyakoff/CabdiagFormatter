package events;

import java.util.HashMap;
import java.util.Map;


public class EventBuilder {
    private Object eventType;
    private Object source;
    private final Map properties;


    public EventBuilder() {
        properties = new HashMap<>();
        eventType = Event.UNDEFINED_TYPE;
    }


    public EventBuilder setType(Object eventType) {
        this.eventType = eventType;
        return this;
    }


    public EventBuilder setSource(Object source) {
        this.source = source;
        return this;
    }


    public EventBuilder addProperty(Object type, Object value) {
        properties.put(type, value);
        return this;
    }


    public Event create() {
        return new Event(eventType, source, properties);
    }
}
