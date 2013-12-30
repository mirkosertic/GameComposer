package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromScene;
import de.mirkosertic.gameengine.core.SystemTick;
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
        aEventManager.register(null, KillProcessesForInstance.class, new GameEventListener<KillProcessesForInstance>() {
            public void handleGameEvent(KillProcessesForInstance aEvent) {
                theManager.killProcessesForInstance(aEvent.instanceProperty().get());
            }
        });
        aEventManager.register(null, StartProcess.class, new GameEventListener<StartProcess>() {
            public void handleGameEvent(StartProcess aEvent) {
                theManager.start(aEvent.processProperty().get());
            }
        });
        aEventManager.register(null, GameObjectInstanceRemovedFromScene.class, new GameEventListener<GameObjectInstanceRemovedFromScene>() {
            public void handleGameEvent(GameObjectInstanceRemovedFromScene aEvent) {
                theManager.instanceRemovedFromScene(aEvent.instanceProperty().get());
            }
        });
        aEventManager.register(null, SystemTick.class, new GameEventListener<SystemTick>() {
            @Override
            public void handleGameEvent(SystemTick aEvent) {
                theManager.proceedGame(aEvent.gameTimeProperty().get(), aEvent.elapsedTimeSinceLastLoopProperty().get());
            }
        });
        return theManager;
    }
}
