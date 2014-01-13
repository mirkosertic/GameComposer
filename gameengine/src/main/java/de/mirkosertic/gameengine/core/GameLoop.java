package de.mirkosertic.gameengine.core;

public class GameLoop implements Runnable {

    private boolean shutdownSignal;
    private final GameView humanGameView;
    private final GameScene scene;
    private final GameRuntime runtime;
    private final long startTime;
    private long lastInvocation;
    private long totalTicks;

    GameLoop(GameScene aScene, GameView aHumanGameView, GameRuntime aRuntime) {
        humanGameView = aHumanGameView;
        shutdownSignal = false;
        runtime = aRuntime;
        scene = aScene;
        startTime = System.currentTimeMillis();
    }

    public GameView getHumanGameView() {
        return humanGameView;
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

            // The game systems like physics or process need a chance to do something useful.
            for (GameSystem theSystem : runtime.getSystems()) {
                theSystem.proceedGame(totalTicks, theGameTime, theElapsedTime);
            }

            // Trigger rerendering of game view
            humanGameView.renderGame(theGameTime, theElapsedTime, scene);

            lastInvocation = theCurrentTime;
            totalTicks++;
        }
    }

    public void shutdown() {
        shutdownSignal = true;
    }

    public boolean isShutdown() {
        return shutdownSignal;
    }
}
