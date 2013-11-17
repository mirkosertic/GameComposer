package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.core.GameLoopRun;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;

public class GameProcessManagerFactory {

    public GameProcessManager create(GameEventManager aEventManager) {
        final GameProcessManager theManager = new GameProcessManager();
        aEventManager.register(null, StartProcess.class, new GameEventListener<StartProcess>() {
            public void handleGameEvent(StartProcess aEvent) {
                theManager.start(aEvent.processProperty().get());
            }
        });
        aEventManager.register(null, GameLoopRun.class, new GameEventListener<GameLoopRun>() {
            @Override
            public void handleGameEvent(GameLoopRun aEvent) {
                theManager.proceedGame(aEvent.gameTimeProperty().get(), aEvent.elapsedTimeSinceLastLoopProperty().get());
            }
        });
        return theManager;
    }
}
