package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventListener;

public class GameLoopFactory {

    public GameLoop create(GameScene aGameScene, GameView aHumanGameView, GameRuntime aRuntime, GameLoopThrottle aThrottle) {
        final GameLoop theLoop = new GameLoop(aGameScene, aHumanGameView, aRuntime, aThrottle);
        aRuntime.getEventManager().register(null, GameShutdown.class, new GameEventListener<GameShutdown>() {
            public void handleGameEvent(GameShutdown aEvent) {
                theLoop.shutdown();
            }
        });
        return theLoop;
    }
}
