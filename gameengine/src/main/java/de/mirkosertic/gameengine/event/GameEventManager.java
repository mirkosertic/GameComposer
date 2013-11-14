package de.mirkosertic.gameengine.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEventManager implements GameEventListener {

    private final Map<Class<GameEvent>, List<GameEventListener>> registeredListeners;

    public GameEventManager() {
        registeredListeners = new HashMap<Class<GameEvent>, List<GameEventListener>>();
    }

    public <T extends GameEvent> void register(Object aOwningInstance, Class<T> aEvent, GameEventListener<T> aEventListener) {
        List<GameEventListener> theListener = registeredListeners.get(aEvent);
        if (theListener == null) {
            theListener = new ArrayList<GameEventListener>();
            registeredListeners.put((Class<GameEvent>) aEvent, theListener);
        }
        theListener.add(aEventListener);
    }

    public void fire(GameEvent aEvent) {
        List<GameEventListener> theRegisteredListener = registeredListeners.get(GameEvent.class);
        if (theRegisteredListener != null) {
            for (GameEventListener theListener : theRegisteredListener) {
                theListener.handleGameEvent(aEvent);
            }
        }

        theRegisteredListener = registeredListeners.get(aEvent.getClass());
        if (theRegisteredListener != null) {
            for (GameEventListener theListener : theRegisteredListener) {
                theListener.handleGameEvent(aEvent);
            }
        }
    }

    @Override
    public void handleGameEvent(GameEvent aEvent) {
        fire(aEvent);
    }

    public void clearListener() {
        registeredListeners.clear();
    }
}