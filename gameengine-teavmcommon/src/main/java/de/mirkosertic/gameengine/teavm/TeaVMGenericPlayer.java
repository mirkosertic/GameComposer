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
package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.Version;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameLoop;
import de.mirkosertic.gameengine.core.GameLoopFactory;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.PlaySceneStrategy;
import de.mirkosertic.gameengine.network.DefaultNetworkConnector;
import de.mirkosertic.gameengine.network.NetworkConnector;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.TouchIdentifier;
import de.mirkosertic.gameengine.type.TouchPosition;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArrayReader;
import org.teavm.jso.dom.events.EventTarget;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

public class TeaVMGenericPlayer {

    private static final Window window = Window.current();
    private static final HTMLDocument document = window.getDocument();

    private GameLoopFactory gameLoopFactory;
    private PlaySceneStrategy runSceneStrategy;
    private TeaVMGameRuntimeFactory runtimeFactory;
    private TeaVMGameSceneLoader sceneLoader;
    private Game game;
    private NetworkConnector networkConnector;
    private boolean fullscreenRequested;
    private HTMLCanvasElement canvasElement;

    public TeaVMGenericPlayer() {
    }

    protected TeaVMGameSceneLoader createSceneLoader(TeaVMGameRuntimeFactory aRuntimeFactory) {
        return new TeaVMGameSceneLoader(aRuntimeFactory);
    }

    protected TeaVMGameLoader createGameLoader() {
        return new TeaVMGameLoader();
    }

    protected TeaVMGameResourceLoader createResourceLoader(String aSceneID) {
        return new TeaVMGameResourceLoader(aSceneID);
    }

    protected void loadOtherSceneFromWithinGame(Game aGame, String aSceneID) {
        sceneLoader.loadFromServer(aGame, aSceneID, createResourceLoader(aSceneID)).thenContinue(
                aResult -> {
                    playScene(aResult);
                });
    }

    public void boot(HTMLCanvasElement aCanvas) {

        canvasElement = aCanvas;

        TeaVMLogger.info("Starting GameEngine " + Version.VERSION);

        TeaVMLogger.info("Booting game runtime");

        gameLoopFactory = new GameLoopFactory();
        runtimeFactory = new TeaVMGameRuntimeFactory(
                !window.getLocation().getFullURL().contains("nothreading"),
                window.getLocation().getFullURL().contains("profiling"));

        // Initialize PIXI
        final Renderer theRenderer;
        if (window.getLocation().getFullURL().contains("canvas")) {
            theRenderer = Renderer.canvasRenderer(320, 200, aCanvas);
        } else {
            theRenderer = Renderer.autodetectRenderer(320, 200, aCanvas);
        }

        switch (theRenderer.getType()) {
        case Renderer.TYPE_WEBGL:
            TeaVMLogger.info("Using: WebGL Renderer");
            break;
        case Renderer.TYPE_CANVAS:
            TeaVMLogger.info("Using: HTML5 Canvas Renderer");
            break;
        default:
            TeaVMLogger.info("Using: Unknown Renderer");
            break;
        }

        sceneLoader = createSceneLoader(runtimeFactory);

        createGameLoader().loadFromServer().thenContinue(aResult -> {
            game = aResult;

            if (aResult.enableNetworkingProperty().get()) {
                String theConnectionID = window.getLocation().getHash();
                if (theConnectionID == null || theConnectionID.isEmpty()) {
                    // No connection id provided, we will start a new one
                    theConnectionID = "game" + System.currentTimeMillis();
                    window.getLocation().setHash(theConnectionID);
                } else {
                    // Extract the hash character
                    theConnectionID = theConnectionID.substring(1);
                }

                boolean theTruncateDB = "?truncate".equals(window.getLocation().getSearch());

                String theFirebaseURL = aResult.fireBaseURLProperty().get();

                TeaVMLogger.info("Enabling Firebase Networking with URL " + theFirebaseURL+", truncate = " + theTruncateDB);
                networkConnector = new TeaVMFirebaseNetworkConnector(theFirebaseURL, theConnectionID, theTruncateDB);
            } else {
                networkConnector = new DefaultNetworkConnector();
            }

            runSceneStrategy = new PlaySceneStrategy(runtimeFactory, gameLoopFactory, networkConnector) {

                private TeaVMGameView gameView;

                @Override
                protected void loadOtherScene(String aSceneId) {
                    loadOtherSceneFromWithinGame(game, aSceneId);
                }

                @Override
                protected Size getScreenSize() {
                    return new Size(window.getInnerWidth(), window.getInnerHeight());
                }

                @Override
                protected GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector) {
                    if (gameView == null) {
                        gameView = new TeaVMGameView(aGameRuntime, aCamera, aGestureDetector, theRenderer);
                    } else {
                        gameView.prepareNewScene(aGameRuntime, aCamera, aGestureDetector);
                    }
                    gameView.setSize(getScreenSize());
                    return gameView;
                }

                @Override
                public void handleResize() {
                    Size theCurrentSize = getScreenSize();
                    getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new SetScreenResolution(theCurrentSize));
                    gameView.setSize(theCurrentSize);
                }
            };

            String theSceneId = aResult.defaultSceneProperty().get();

            TeaVMLogger.info("Loading scene " + theSceneId);

            sceneLoader.loadFromServer(game, theSceneId, createResourceLoader(theSceneId)).thenContinue(
                    aResult1 -> {
                        playScene(aResult1);
                    });
        }).catchError((aResult, aOptionalException) -> TeaVMLogger.error("Failed to load scene : " + aResult));

        EventTarget documentEventTarget = document;
        documentEventTarget.addEventListener("keydown", aEvent -> keyPressed((TeaVMKeyEvent) aEvent), false);
        documentEventTarget.addEventListener("keyup", aEvent -> keyReleased((TeaVMKeyEvent) aEvent), false);
        aCanvas.addEventListener("touchstart", aEvent -> touchStarted((TeaVMTouchEvent) aEvent), false);
        aCanvas.addEventListener("touchend", aEvent -> touchEnded((TeaVMTouchEvent) aEvent), false);
        aCanvas.addEventListener("touchcancel", aEvent -> touchCanceled((TeaVMTouchEvent) aEvent), false);
        aCanvas.addEventListener("touchmove", aEvent -> touchMoved((TeaVMTouchEvent) aEvent), false);
        aCanvas.addEventListener("mousedown", aEvent -> mouseDown((TeaVMMouseEvent) aEvent));
        aCanvas.addEventListener("mouseup", aEvent -> mouseUp((TeaVMMouseEvent) aEvent));

        window.addEventListener("resize", evt -> {
            if (runSceneStrategy.hasGameLoop()) {
                runSceneStrategy.handleResize();
            }
        }, true);
    }

    private void requestFullScreen() {
        if (!fullscreenRequested) {

            TeaVMLogger.info("Requesting fullscreen mode");
            TeaVMWindow.requestFullScreen(canvasElement);
            fullscreenRequested = true;
        }
    }

    private void mouseDown(TeaVMMouseEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            requestFullScreen();
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mousePressed(
                    new Position(aEvent.getClientX(), aEvent.getClientY())
            );
        }
    }

    private void mouseUp(TeaVMMouseEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            requestFullScreen();
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mouseReleased(
                    new Position(aEvent.getClientX(), aEvent.getClientY())
            );
        }
    }

    private void keyPressed(TeaVMKeyEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            requestFullScreen();
            int theCode = aEvent.getKeyCode();
            GameKeyCode theKeyCode = TeaVMKeyCodeTranslator.translate(theCode);
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyPressed(theKeyCode);
            runSceneStrategy.getRunningGameLoop().getScene().getRuntime().getLogger().info("KeyEvent keyPressed " + theCode);
        }
    }

    private void keyReleased(TeaVMKeyEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            requestFullScreen();
            int theCode = aEvent.getKeyCode();
            GameKeyCode theKeyCode = TeaVMKeyCodeTranslator.translate(theCode);
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyReleased(theKeyCode);
            runSceneStrategy.getRunningGameLoop().getScene().getRuntime().getLogger().info("KeyEvent keyReleased " + theCode);
        }
    }

    private TouchPosition[] toArray(JSArrayReader<TeaVMTouch> aTouches) {
        TouchPosition[] thePositions = new TouchPosition[aTouches.getLength()];
        for (int i=0;i<aTouches.getLength();i++) {
            TeaVMTouch theTouch = aTouches.get(i);
            thePositions[i] = new TouchPosition(new TouchIdentifier(theTouch.getIdentifier()), theTouch.getScreenX(), theTouch.getScreenY());
        }
        return thePositions;
    }

    private void touchStarted(TeaVMTouchEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            requestFullScreen();
            GestureDetector theDetector = runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchStarted(toArray(aEvent.getTouches()));
        }
    }

    private void touchEnded(TeaVMTouchEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            requestFullScreen();
            GestureDetector theDetector = runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchEnded(toArray(aEvent.getTouches()));
        }
    }

    private void touchMoved(TeaVMTouchEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            GestureDetector theDetector = runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchMoved(toArray(aEvent.getTouches()));
        }
    }

    private void touchCanceled(TeaVMTouchEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            GestureDetector theDetector = runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchCanceled(toArray(aEvent.getTouches()));
        }
    }

    private void runSingleStep(final GameLoop aGameLoop) {
        if (!aGameLoop.isShutdown()) {
            aGameLoop.singleRun();
            TeaVMWindow.requestAnimationFrame(aTimeDelta -> runSingleStep(aGameLoop));
        }
    }

    private void playScene(GameScene aGameScene) {
        runSceneStrategy.playScene(aGameScene).thenContinue(aResult -> {
            runSingleStep(runSceneStrategy.getRunningGameLoop());
        });
    }
}