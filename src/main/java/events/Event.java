package events;

import java.util.HashMap;
import java.util.Map;


public class Event {
    public static final Object UNDEFINED_TYPE = new Object();
    private final Object type;
    private  Object source;
    private final Map<Object, Object> properties;


    public Event(Object type, Object source, Map properties) {
        this.type = type;
        this.source = source;
        this.properties = properties;
    }


    public Event(Object type) {
        this.type = type;
        properties = new HashMap<>();
    }


    public void setSource(Object source) {
        this.source = source;
    }


    public Object type() {
        return type;
    }


    public Object source() {
        return source;
    }


    public Object property(Object type) {
        return properties.get(type);
    }
}
