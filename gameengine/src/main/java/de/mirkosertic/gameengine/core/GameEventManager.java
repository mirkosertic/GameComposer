package de.mirkosertic.gameengine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEventManager {

    class RegisteredListener {
        GameObjectInstance owningInstance;
        GameEventListener eventListener;
    }

    private Map<Class<GameEvent>, List<GameEventListener>> registeredListeners;

    public GameEventManager() {
        registeredListeners = new HashMap<Class<GameEvent>, List<GameEventListener>>();
    }

    public <T extends GameEvent> void register(GameObjectInstance aOwningInstance, Class<T> aEvent, GameEventListener<T> aEventListener) {
        List<GameEventListener> theListener = registeredListeners.get(aEvent);
        if (theListener == null) {
            theListener = new ArrayList<GameEventListener>();
            registeredListeners.put((Class<GameEvent>) aEvent, theListener);
        }
        theListener.add(aEventListener);
    }

    public void fire(GameEvent aEvent) {
        List<GameEventListener> theRegisteredListener = registeredListeners.get(aEvent.getClass());
        if (theRegisteredListener != null) {
            for (GameEventListener theListener : theRegisteredListener) {
                theListener.handleGameEvent(aEvent);
            }
        }
    }

    public void clearListener() {
        registeredListeners.clear();
    }
}