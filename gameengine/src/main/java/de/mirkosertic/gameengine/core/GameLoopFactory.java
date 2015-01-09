package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventListener;

public class GameLoopFactory {

    public GameLoop create(GameScene aGameScene, GameView aHumanGameView, GameRuntime aRuntime) {
        final GameLoop theLoop = new GameLoop(aGameScene, aHumanGameView, aRuntime);
        aRuntime.getEventManager().register(null, SceneShutdown.class, new GameEventListener<SceneShutdown>() {
            @Override
            public void handleGameEvent(SceneShutdown aEvent) {
                theLoop.shutdown();
            }
        });
        return theLoop;
    }
}
