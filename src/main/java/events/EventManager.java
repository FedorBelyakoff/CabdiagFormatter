package events;

import java.util.*;


public class EventManager {
    private static final EventManager INSTANCE = new EventManager();


    public static EventManager instance() {
        return INSTANCE;
    }


    private EventManager() {
        subscribers = new HashMap<>();
        updatingStack = new ArrayDeque<>();
    }


    private final Map<Publisher, SubscribersRegister> subscribers;
    private final Deque<Object> updatingStack;


    public void addObserver(Subscriber subscriber, Publisher publisher) {
        addObserver(subscriber, publisher, Event.UNDEFINED_TYPE);
    }


    public void addObserver(Subscriber s, Publisher p, Object eventType) {
        if (!subscribers.containsKey(p)) {
            subscribers.put(p, new SubscribersRegister(p));
        }
        SubscribersRegister reg = subscribers.get(p);
        reg.add(s, eventType);
    }


    public void removeObserver(Subscriber sub, Publisher pub) {
        if (!subscribers.containsKey(pub)) {
            return;
        }

        SubscribersRegister subs = subscribers.get(pub);
        subs.remove(sub);
    }


    public void removeObserver(Subscriber sub, Publisher pub, Object eventType) {
        if (!subscribers.containsKey(pub)) {
            return;
        }

        SubscribersRegister subs = subscribers.get(pub);
        subs.remove(sub, eventType);
    }


    public void notifyObservers(Publisher source, Event e) {
        if (!subscribers.containsKey(source)
                         || updatingStack.contains(source)) {
            return;
        }
        updatingStack.push(source);
        SubscribersRegister register = subscribers.get(source);

        Object eventType = e.type();
        if (register.hasType(eventType)) {
            update(e, register.subscribers(eventType));
        }
        if (register.hasType(Event.UNDEFINED_TYPE)
                         && eventType != Event.UNDEFINED_TYPE) {
            update(e, register.subscribers(Event.UNDEFINED_TYPE));
        }
        updatingStack.pop();
    }


    private void update(Event e, Set<Subscriber> subsFromEvent) {
        subsFromEvent.forEach(
                         s -> {
                             if (!updatingStack.contains(s)) {
                                 s.update(e);
                             }
                         }
        );
    }


    public void notifyObservers(Publisher source, Object arg) {

    }
}
