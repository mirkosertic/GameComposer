package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventListener;

public class GameLoopFactory {

    public GameLoop create(GameScene aGameScene, GameView aHumanGameView, GameRuntime aRuntime, GameLoopThrottle aThrottle) {
        final GameLoop theLoop = new GameLoop(aGameScene, aHumanGameView, aRuntime, aThrottle);
        aRuntime.getEventManager().register(null, GameShutdownEvent.class, new GameEventListener<GameShutdownEvent>() {
            public void handleGameEvent(GameShutdownEvent aEvent) {
                theLoop.shutdown();
            }
        });
        return theLoop;
    }
}
