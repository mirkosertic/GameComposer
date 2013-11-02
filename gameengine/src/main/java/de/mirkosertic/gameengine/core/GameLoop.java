package de.mirkosertic.gameengine.core;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameLoop implements Runnable {

    private AtomicBoolean shutdownSignal;
    private Set<GameView> views;
    private GameScene scene;
    private GameRuntime runtime;

    GameLoop(GameScene aScene, GameView aHumanGameView, GameRuntime aRuntime) {
        views = new HashSet<GameView>();
        views.add(aHumanGameView);
        shutdownSignal = new AtomicBoolean(false);
        runtime = aRuntime;
        scene = aScene;
    }

    public void run() {
        long theStartTime = System.currentTimeMillis();
        long theLastInvocation = theStartTime;
        long theCurrentTime;

        while (!shutdownSignal.get() && !Thread.currentThread().isInterrupted()) {
            theCurrentTime = System.currentTimeMillis();
            long theGameTime = theCurrentTime - theStartTime;
            long theElapsedTime = theCurrentTime - theLastInvocation;
            if (theElapsedTime > 0) {

                // Every game system gets a chance to do something
                for (GameSystem theSystem : runtime.getSystems()) {
                    theSystem.proceedGame(theGameTime, theElapsedTime);
                }

                // Trigger re-rendering of all game views
                for (GameView aView : views) {
                    aView.renderGame(theGameTime, theElapsedTime, scene);
                }

                theLastInvocation = theCurrentTime;
            }
        }
    }

    public void shutdown() {
        shutdownSignal.set(true);
    }
}