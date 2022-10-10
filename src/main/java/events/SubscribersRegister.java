package events;

import java.util.*;
import java.util.stream.Collectors;


class SubscribersRegister {
    private final Publisher source;
    private final Map<Object, Set<Subscriber>> subscribers;


    public SubscribersRegister(Publisher source) {
        this.source = source;
        subscribers = new HashMap<>();
    }


    Publisher getSource() {
        return source;
    }


    void add(Subscriber s, Object eventType) {
        if (!subscribers.containsKey(eventType)) {
            subscribers.put(eventType, new HashSet<>());
        }
        Set<Subscriber> subscribersSet = subscribers.get(eventType);
        subscribersSet.add(s);
    }


    Set<Subscriber> subscribers(Object eventType) {
        return subscribers.get(eventType);
    }


    Set<Subscriber> allSubscribers() {
        return subscribers.values().stream()
                         .flatMap(Collection::stream)
                         .collect(Collectors.toSet());
    }


    void remove(Subscriber s) {
        subscribers.forEach(
                         (eventType, subs) -> subs.remove(s)
        );
    }


    void remove(Subscriber s, Object eventType) {
        if (subscribers.containsKey(eventType)) {
            subscribers.get(eventType).remove(s);
        }
    }


    public boolean hasType(Object eventType) {
        return subscribers.containsKey(eventType);
    }
}
