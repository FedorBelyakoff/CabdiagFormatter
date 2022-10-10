package events;

public interface Publisher {
    default void notifySubscribers() {
        notifySubscribers(new Event(Event.UNDEFINED_TYPE));
    }

    default void notifySubscribers(Event e) {
        if (null == e.source()) {
            e.setSource(this);
        }
        EventManager.instance().notifyObservers(this, e);
    }
    default void addSubscriber(Subscriber s) {
        EventManager.instance().addObserver(s, this);
    }

    default void removeSubscriber(Subscriber o) {
        EventManager.instance().removeObserver(o, this);
    }
}
