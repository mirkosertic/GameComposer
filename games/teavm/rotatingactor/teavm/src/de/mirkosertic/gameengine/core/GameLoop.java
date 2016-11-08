/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.event.SystemException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLoop implements Runnable {

    private boolean shutdownSignal;
    private final GameView humanGameView;
    private final GameScene scene;
    private final GameRuntime runtime;
    private long lastInvocation;

    private final RuntimeStatistics statistics;
    private GameView[] gameViews;

    GameLoop(GameScene aScene, GameView aHumanGameView, GameRuntime aRuntime) {
        humanGameView = aHumanGameView;
        shutdownSignal = false;
        runtime = aRuntime;
        scene = aScene;
        gameViews = new GameView[] {aHumanGameView};
        statistics = new RuntimeStatistics();
    }

    public void addGameView(GameView aNewGameView) {
        List<GameView> theViews = ArrayUtils.asList(gameViews);
        theViews.add(aNewGameView);
        gameViews = theViews.toArray(new GameView[theViews.size()]);
    }

    public GameView getHumanGameView() {
        return humanGameView;
    }

    public GameScene getScene() {
        return scene;
    }

    @Override
    public void run() {
        while (!shutdownSignal) {
            singleRun();
        }
    }

    public void singleRunOnlyUpdateGameView() {
        if  (lastInvocation == 0) {
            // Initial state, we skip this to prevent computation errors
            lastInvocation = System.currentTimeMillis();
            return;
        }
        long theCurrentTime = System.currentTimeMillis();
        long theGameTime = theCurrentTime - statistics.getStartTime();
        long theElapsedTime = theCurrentTime - lastInvocation;
        if (theElapsedTime > 0) {

            try {
                long theNumberOfTicks = statistics.incrementTicks();

                statistics.beginGameLoop();

                for (GameView theView : gameViews) {
                    // Trigger rerendering of game view
                    theView.renderGame(theGameTime, theElapsedTime, scene, statistics);
                }

                lastInvocation = theCurrentTime;
            } catch (Exception e) {
                runtime.getEventManager().fire(new SystemException(e));
            } finally {
                statistics.endGameLoop();
            }
        }
    }

    public void singleRun() {
        if  (lastInvocation == 0) {
            // Initial state, we skip this to prevent computation errors
            lastInvocation = System.currentTimeMillis();
            // Instead, we fill fire the SceneStarted event

            runtime.getEventManager().fire(new SceneStarted());
            return;
        }
        long theCurrentTime = System.currentTimeMillis();
        long theGameTime = theCurrentTime - statistics.getStartTime();
        long theElapsedTime = theCurrentTime - lastInvocation;
        if (theElapsedTime > 0) {

            try {
                long theNumberOfTicks = statistics.incrementTicks();

                statistics.beginGameLoop();

                // First give the gamesystems the change to do something
                Map<GameSystem, GameSystemWork> theFutures = new HashMap<>();
                // The game systems like physics or process need a chance to do something useful.
                for (GameSystem theSystem : runtime.getSystems()) {
                    GameSystemWork theWork = theSystem.proceedGame(theNumberOfTicks, theGameTime, theElapsedTime);
                    theWork.runInFrame();
                    theFutures.put(theSystem, theWork);
                };

                // Trigger rerendering of game view
                for (GameView theGameView : gameViews) {
                    theGameView.renderGame(theGameTime, theElapsedTime, scene, statistics);
                }

                // Finally wait for the game systems till they have completed their job
                for (Map.Entry<GameSystem, GameSystemWork> theEntry : theFutures.entrySet()) {
                    theEntry.getValue().runAfterFrame();
                }

                lastInvocation = theCurrentTime;

            } catch (Exception e) {
                runtime.getEventManager().fire(new SystemException(e));
            } finally {
                statistics.endGameLoop();
            }
        }
    }

    public void shutdown() {
        shutdownSignal = true;
    }

    public boolean isShutdown() {
        return shutdownSignal;
    }

    RuntimeStatistics getStatistics() {
        return statistics;
    }
}