package de.mirkosertic.gameengine.core;

public class GameLoopFactory {

    public GameLoop create(GameScene aGameScene, GameView aHumanGameView, GameRuntime aRuntime) {
        final GameLoop theLoop = new GameLoop(aGameScene, aHumanGameView, aRuntime);
        aRuntime.getEventManager().register(null, GameShutdownEvent.class, new GameEventListener<GameShutdownEvent>() {
            public void handleGameEvent(GameShutdownEvent aEvent) {
                theLoop.shutdown();
            }
        });
        return theLoop;
    }
}
