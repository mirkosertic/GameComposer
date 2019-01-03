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

import de.mirkosertic.bytecoder.api.Export;
import de.mirkosertic.bytecoder.api.web.EventTarget;
import de.mirkosertic.bytecoder.api.web.HTMLCanvasElement;
import de.mirkosertic.bytecoder.api.web.HTMLDocument;
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
import de.mirkosertic.gameengine.network.DefaultNetworkConnector;
import de.mirkosertic.gameengine.network.NetworkConnector;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;
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

    @Export("main")
    public static void main(String[] args) {
        window = Window.window();
        HTMLDocument document = window.document();
        HTMLCanvasElement aCanvas = (HTMLCanvasElement) document.getElementById("html5canvas");

        BytecoderLogger.INSTANCE.info("Starting GameEngine " + Version.VERSION);

        BytecoderLogger.INSTANCE.info("Booting game runtime");

        gameLoopFactory = new GameLoopFactory();
        runtimeFactory = new BytecoderGameRuntimeFactory();
        soundSystemFactory = new BytecoderGameSoundSystemFactory();

        // Initialize PIXI
        final Renderer theRenderer = Renderer.autodetectRenderer(320, 200, aCanvas);

        switch (theRenderer.type()) {
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
                protected void loadOtherScene(String aSceneId) {
                    loadOtherSceneFromWithinGame(game, aSceneId);
                }

                @Override
                protected Size getScreenSize() {
                    return new Size(window.innerWidth(), window.innerHeight());
                }

                @Override
                protected GameView getOrCreateCurrentGameView(
                        GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector) {
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
                    Size theCurrentSize = getScreenSize();
                    getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new SetScreenResolution(theCurrentSize));
                    gameView.setSize(theCurrentSize);
                }
            };

            String theSceneId = aResult.defaultSceneProperty().get();

            sceneLoader.loadFromServer(game, theSceneId, new BytecoderGameResourceLoader(theSceneId)).thenContinue(
                    aResult1 -> {
                        playScene(aResult1);
                    });
        }).catchError((aResult, aOptionalException) -> BytecoderLogger.INSTANCE.error("Failed to load scene : " + aResult));

        EventTarget documentEventTarget = document;

        window.addEventListener("resize", evt -> {
            if (runSceneStrategy.hasGameLoop()) {
                runSceneStrategy.handleResize();
            }
        });
    }

    private static void loadOtherSceneFromWithinGame(Game aGame, String aSceneID) {
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

    private static void playScene(GameScene aGameScene) {
        BytecoderLogger.INSTANCE.info("Playing scene");
        runSceneStrategy.playScene(aGameScene).thenContinue(aResult -> {
            runSingleStep(runSceneStrategy.getRunningGameLoop());
        });
    }
}
