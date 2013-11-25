package de.mirkosertic.gameengine.core;

public class GameLoop implements Runnable {

    private boolean shutdownSignal;
    private final GameView[] views;
    private final GameScene scene;
    private final GameRuntime runtime;
    private final long startTime;
    private long lastInvocation;

    GameLoop(GameScene aScene, GameView aHumanGameView, GameRuntime aRuntime) {
        views = new GameView[] {aHumanGameView};
        shutdownSignal = false;
        runtime = aRuntime;
        scene = aScene;
        startTime = System.currentTimeMillis();
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

            // The game systems like physics or process can react on this event
            // and do something useful...
            runtime.getEventManager().fire(new GameLoopRun(theGameTime, theElapsedTime));

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

    public boolean isShutdown() {
        return shutdownSignal;
    }
}
