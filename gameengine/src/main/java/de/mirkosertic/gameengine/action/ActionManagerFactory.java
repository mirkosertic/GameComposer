package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;

public class ActionManagerFactory {

    public ActionManager create(GameScene aScene, GameEventManager aEventManager) {
        final ActionManager theManager = new ActionManager(aScene);
        aEventManager.register(null, GameEvent.class, new GameEventListener<GameEvent>() {
            @Override
            public void handleGameEvent(GameEvent aEvent) {
                theManager.onEvent(aEvent);
            }
        });
        return theManager;
    }
}
