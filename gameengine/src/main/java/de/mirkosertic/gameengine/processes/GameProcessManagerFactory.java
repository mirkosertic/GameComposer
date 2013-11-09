package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;

public class GameProcessManagerFactory {

    public GameProcessManager create(GameEventManager aEventManager) {
        final GameProcessManager theManager = new GameProcessManager();
        aEventManager.register(null, StartProcessEvent.class, new GameEventListener<StartProcessEvent>() {
            public void handleGameEvent(StartProcessEvent aEvent) {
                theManager.start(aEvent.getProcess());
            }
        });
        return theManager;
    }
}
