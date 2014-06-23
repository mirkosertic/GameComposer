package de.mirkosertic.gameengine.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mirkosertic.gameengine.ArrayUtils;

public class GameEventManager implements GameEventListener {

    private final Map<Class<GameEvent>, GameEventListener[]> registeredListeners;

    public GameEventManager() {
        registeredListeners = new HashMap<>();
    }

    public void register(Object aOwningInstance, Class aEvent, GameEventListener aEventListener) {
        GameEventListener[] theListener = registeredListeners.get(aEvent);
        if (theListener == null) {
            theListener = new GameEventListener[] { aEventListener };
            registeredListeners.put(aEvent, theListener);
            return;
        }
        List<GameEventListener> theListenerList = ArrayUtils.asList(theListener);
        theListenerList.add(aEventListener);
        registeredListeners.put(aEvent, theListenerList.toArray(new GameEventListener[theListenerList.size()]));
    }

    public void fire(GameEvent aEvent) {
        try {
            GameEventListener[] theRegisteredListener = registeredListeners.get(GameEvent.class);
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
        } catch (Exception e) {
            if (aEvent instanceof SystemException) {
                throw new RuntimeException("Error dispatching system exception", e);
            } else {
                fire(new SystemException(e));
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
