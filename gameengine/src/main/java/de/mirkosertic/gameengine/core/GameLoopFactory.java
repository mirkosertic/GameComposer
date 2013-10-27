package de.mirkosertic.gameengine.core;

public class GameLoopFactory {

    public GameLoop create(GameScene aGameScene, GameView aHumanGameView, GameRuntime aRuntime) {
        final GameLoop theLoop = new GameLoop(aGameScene, aHumanGameView, aRuntime);
        aRuntime.getEventManager().register(null, ShutdownGameEvent.class, new GameEventListener<ShutdownGameEvent>() {
            public void handleGameEvent(ShutdownGameEvent aEvent) {
                theLoop.shutdown();
            }
        });
        return theLoop;
    }
}
