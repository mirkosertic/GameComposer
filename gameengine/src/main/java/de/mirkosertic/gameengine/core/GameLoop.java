package de.mirkosertic.gameengine.core;

public class GameLoop implements Runnable {

    private boolean shutdownSignal;
    private final GameView[] views;
    private final GameScene scene;
    private final GameRuntime runtime;
    private final long startTime;
    private long lastInvocation;
    private final GameLoopThrottle throttle;

    GameLoop(GameScene aScene, GameView aHumanGameView, GameRuntime aRuntime, GameLoopThrottle aThrottle) {
        views = new GameView[] {aHumanGameView};
        shutdownSignal = false;
        runtime = aRuntime;
        scene = aScene;
        startTime = System.currentTimeMillis();
        throttle = aThrottle;
    }

    public GameScene getScene() {
        return scene;
    }

    public void run() {
        while (!shutdownSignal) {
            singleRun();
        }
    }

    public void singleRun() {
        long theCurrentTime = System.currentTimeMillis();
        long theGameTime = theCurrentTime - startTime;
        long theElapsedTime = theCurrentTime - lastInvocation;
        if (theElapsedTime > 0) {

            runtime.getEventManager().fire(new GameLoopRunEvent());

            // Every game system gets a chance to do something
            for (GameSystem theSystem : runtime.getSystems()) {
                theSystem.proceedGame(theGameTime, theElapsedTime);
            }

            // Trigger re-rendering of all game views
            for (GameView aView : views) {
                aView.renderGame(theGameTime, theElapsedTime, scene);
            }

            lastInvocation = theCurrentTime;
        }

    }

    public void shutdown() {
        shutdownSignal = true;
    }
}
