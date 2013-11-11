package de.mirkosertic.gameengine.core;

import java.util.HashSet;
import java.util.Set;

public class GameLoop implements Runnable {

    private boolean shutdownSignal;
    private Set<GameView> views;
    private GameScene scene;
    private GameRuntime runtime;
    private long startTime;
    private long lastInvocation;
    private GameLoopThrottle throttle;

    GameLoop(GameScene aScene, GameView aHumanGameView, GameRuntime aRuntime, GameLoopThrottle aThrottle) {
        views = new HashSet<GameView>();
        views.add(aHumanGameView);
        shutdownSignal = false;
        runtime = aRuntime;
        scene = aScene;
        startTime = System.currentTimeMillis();
        throttle = aThrottle;
    }

    public void run() {
        while (!shutdownSignal) {
            long theStart = System.currentTimeMillis();
            singleRun();
            long theDuration = System.currentTimeMillis() - theStart;

            throttle.perhapsThrottleDown(theDuration);
        }
    }

    public void singleRun() {
        long theCurrentTime = System.currentTimeMillis();
        long theGameTime = theCurrentTime - startTime;
        long theElapsedTime = theCurrentTime - lastInvocation;
        if (theElapsedTime > 0) {

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
