/*
 * Copyright 2019 Mirko Sertic
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
package de.mirkosertic.gameengine.bytecoder;

import de.mirkosertic.bytecoder.api.web.ClickEvent;
import de.mirkosertic.bytecoder.api.web.HTMLCanvasElement;
import de.mirkosertic.bytecoder.api.web.HTMLDocument;
import de.mirkosertic.bytecoder.api.web.KeyEvent;
import de.mirkosertic.bytecoder.api.web.Window;
import de.mirkosertic.gameengine.Version;
import de.mirkosertic.gameengine.bytecoder.pixi.Renderer;
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
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.network.DefaultNetworkConnector;
import de.mirkosertic.gameengine.network.NetworkConnector;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

public class BytecoderRenderer {

    private static Window window;
    private static GameLoopFactory gameLoopFactory;
    private static BytecoderGameRuntimeFactory runtimeFactory;
    private static Game game;
    private static PlaySceneStrategy runSceneStrategy;
    private static BytecoderGameSceneLoader sceneLoader;
    private static NetworkConnector networkConnector;
    private static GameSoundSystemFactory soundSystemFactory;

    public static void main(final String[] args) {
        window = Window.window();
        final HTMLDocument document = window.document();
        final HTMLCanvasElement aCanvas = (HTMLCanvasElement) document.getElementById("html5canvas");

        BytecoderLogger.INSTANCE.info("Starting GameEngine " + Version.VERSION);

        BytecoderLogger.INSTANCE.info("Booting game runtime");

        gameLoopFactory = new GameLoopFactory();
        runtimeFactory = new BytecoderGameRuntimeFactory();
        soundSystemFactory = new BytecoderGameSoundSystemFactory();

        // Initialize PIXI
        final Renderer theRenderer = Renderer.autodetectRenderer(320, 200, aCanvas);

        switch (theRenderer.rendererType()) {
        case Renderer.TYPE_WEBGL:
            BytecoderLogger.INSTANCE.info("Using: WebGL Renderer");
            break;
        case Renderer.TYPE_CANVAS:
            BytecoderLogger.INSTANCE.info("Using: HTML5 Canvas Renderer");
            break;
        default:
            BytecoderLogger.INSTANCE.info("Using: Unknown Renderer");
            break;
        }

        sceneLoader = new BytecoderGameSceneLoader(window, runtimeFactory, soundSystemFactory);

        new BytecoderGameLoader(window).loadFromServer().thenContinue(aResult -> {
            game = aResult;

            BytecoderLogger.INSTANCE.info("Loaded game " + game.nameProperty().get());

            networkConnector = new DefaultNetworkConnector();

            runSceneStrategy = new PlaySceneStrategy(runtimeFactory, gameLoopFactory, networkConnector) {

                private BytecoderGameView gameView;

                @Override
                protected void loadOtherScene(final String aSceneId) {
                    loadOtherSceneFromWithinGame(game, aSceneId);
                }

                @Override
                protected Size getScreenSize() {
                    return new Size((int) window.innerWidth(), (int) window.innerHeight());
                }

                @Override
                protected GameView getOrCreateCurrentGameView(
                        final GameRuntime aGameRuntime, final CameraBehavior aCamera, final GestureDetector aGestureDetector) {
                    if (gameView == null) {
                        gameView = new BytecoderGameView(aGameRuntime, aCamera, aGestureDetector, theRenderer);
                    } else {
                        gameView.prepareNewScene(aGameRuntime, aCamera, aGestureDetector);
                    }
                    gameView.setSize(getScreenSize());
                    return gameView;
                }

                @Override
                public void handleResize() {
                    final Size theCurrentSize = getScreenSize();
                    getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new SetScreenResolution(theCurrentSize));
                    gameView.setSize(theCurrentSize);
                }
            };

            String theSceneId = aResult.defaultSceneProperty().get();

            sceneLoader.loadFromServer(game, theSceneId, new BytecoderGameResourceLoader(theSceneId)).thenContinue(
                    new Promise.NoReturnHandler<GameScene>() {
                        @Override
                        public void process(final GameScene aResult) {
                            playScene(aResult);
                        }
                    });
        }).catchError((aResult, aOptionalException) -> BytecoderLogger.INSTANCE.error("Failed to load scene : " + aResult));

        window.addEventListener("resize", evt -> {
            if (runSceneStrategy.hasGameLoop()) {
                runSceneStrategy.handleResize();
            }
        });
        aCanvas.addEventListener("mousedown", aEvent -> mouseDown((ClickEvent) aEvent));
        aCanvas.addEventListener("mouseup", aEvent -> mouseUp((ClickEvent) aEvent));
        document.addEventListener("keydown", aEvent -> keyPressed((KeyEvent) aEvent));
        document.addEventListener("keyup", aEvent -> keyReleased((KeyEvent) aEvent));
    }

    private static void keyPressed(final KeyEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            final int theCode = (int) aEvent.keyCode();
            final GameKeyCode theKeyCode = BytecoderKeyCodeTranslator.translate(theCode);
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyPressed(theKeyCode);
            BytecoderLogger.INSTANCE.info("KeyEvent keyPressed " + theCode);
        }
    }

    private static void keyReleased(final KeyEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            final int theCode = (int) aEvent.keyCode();
            final GameKeyCode theKeyCode = BytecoderKeyCodeTranslator.translate(theCode);
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyReleased(theKeyCode);
            BytecoderLogger.INSTANCE.info("KeyEvent keyReleased " + theCode);
        }
    }

    private static void mouseDown(final ClickEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mousePressed(
                    new Position(aEvent.clientX(), aEvent.clientY())
            );
        }
    }

    private static void mouseUp(final ClickEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mouseReleased(
                    new Position(aEvent.clientX(), aEvent.clientY())
            );
        }
    }


    private static void loadOtherSceneFromWithinGame(final Game aGame, final String aSceneID) {
        sceneLoader.loadFromServer(aGame, aSceneID, new BytecoderGameResourceLoader(aSceneID)).thenContinue(
                aResult -> {
                    playScene(aResult);
                });
    }

    private static void runSingleStep(final GameLoop aGameLoop) {
        if (!aGameLoop.isShutdown()) {
            aGameLoop.singleRun();

            window.requestAnimationFrame(aTimeDelta -> runSingleStep(aGameLoop));
        }
    }

    private static void playScene(final GameScene aGameScene) {
        BytecoderLogger.INSTANCE.info("Playing scene " + aGameScene.nameProperty().get());
        runSceneStrategy.playScene(aGameScene).thenContinue(aResult -> {
            runSingleStep(runSceneStrategy.getRunningGameLoop());
        });
    }
}
