/*
 * Copyright 2017 Mirko Sertic
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
package de.mirkosertic.gameengine.teavmwasm;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import org.teavm.interop.Export;
import org.teavm.interop.Import;

import de.mirkosertic.gameengine.Version;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameLoopFactory;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.PlaySceneStrategy;
import de.mirkosertic.gameengine.network.DefaultNetworkConnector;
import de.mirkosertic.gameengine.type.Size;

public class WASMRenderer {

    private static GameLoopFactory gameLoopFactory;
    private static WASMGameRuntimeFactory runtimeFactory;
    private static Game loadedGame;
    private static PlaySceneStrategy runSceneStrategy;
    private static Size size = new Size(10, 10);

    @Import(module = "engine", name = "requestAnimationFrame")
    public static native void requestAnimationFrame();

    @Import(module = "engine", name = "bootstrap")
    public static native void bootstrap();

    @Import(module = "engine", name = "loadGameScene")
    public static native void loadGameScene(String aGameSceneId);

    @Export(name = "loadGameFromStringPool")
    public static void loadGameFromStringPool(int aPoolID) {
        Map<String, Object> theResult = new JSONParser().fromJSON(WASMStringPool.getStringPool(aPoolID));

        loadedGame = Game.deserialize(theResult);
        WASMLogger.INSTANCE.info("Game loaded : " + loadedGame.nameProperty().get());

        String theStartingScene = loadedGame.defaultSceneProperty().get();
        WASMLogger.INSTANCE.info("Loading default scene " + theStartingScene);

        loadGameScene(theStartingScene);
    }

    @Export(name = "loadGameSceneFromStringPool")
    public static void loadGameSceneFromStringPool(int aPoolID) {
        try {

            Map<String, Object> theResult = new JSONParser().fromJSON(WASMStringPool.getStringPool(aPoolID));

            WASMLogger.INSTANCE.info("Loading scene");

            GameRuntime theRuntime = runtimeFactory.create(new WASMGameResourceLoader(), new WASMGameSoundSystemFactory());
            WASMLogger.INSTANCE.info("Runtime created");

            GameScene theScene = GameScene.deserialize(loadedGame, theRuntime, theResult);

            WASMLogger.INSTANCE.info("Scene loaded : " + theScene.nameProperty().get());

            playScene(theScene);

        } catch (Throwable e) {
            logException(e);
        }
    }

    private static void playScene(GameScene aScene) {
        try {
            runSceneStrategy.playScene(aScene).thenContinue(aResult -> {
                requestAnimationFrame();
            });
        } catch (Throwable e) {
            logException(e);
        }
    }

    @Export(name = "runSingleStep")
    public static void runSingleStep() {
        if (runSceneStrategy != null && runSceneStrategy.hasGameLoop() && !runSceneStrategy.getRunningGameLoop().isShutdown()) {
            WASMLogger.INSTANCE.info("Trying to run a single step");
            runSceneStrategy.getRunningGameLoop().singleRun();
            WASMLogger.INSTANCE.info("Single step run");
            requestAnimationFrame();
        }
    }

    public static void main(String[] args) {
        Integer.parseInt("10");
        Long.parseLong("10");
        Double.parseDouble("10");
        WASMLogger.INSTANCE.info("Gameengine starting with version " + Version.VERSION);

        WASMStringPool theStringPool = new WASMStringPool();

        gameLoopFactory = new GameLoopFactory();
        runtimeFactory = new WASMGameRuntimeFactory();
        runSceneStrategy = new PlaySceneStrategy(runtimeFactory, gameLoopFactory, new DefaultNetworkConnector()) {

            private WASMGameView gameView;

            @Override
            protected void loadOtherScene(String aSceneId) {
            }

            @Override
            protected Size getScreenSize() {
                return size;
            }

            @Override
            protected GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector) {
                if (gameView == null) {
                    gameView = new WASMGameView(aGameRuntime, aCamera, aGestureDetector);
                } else {
                    gameView.prepareNewScene(aGameRuntime, aCamera, aGestureDetector);
                }
                gameView.setCurrentScreenSize(getScreenSize());
                return gameView;
            }

            @Override
            public void handleResize() {
                Size theCurrentSize = getScreenSize();
                getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new SetScreenResolution(theCurrentSize));
                gameView.setCurrentScreenSize(theCurrentSize);
            }
        };


        bootstrap();
        // We are done here, waiting for the loadGameFromStringPool callback invoked
        // by JavaScript side
    }

    private static void logException(Throwable e) {
        try {
            ByteArrayOutputStream theOS = new ByteArrayOutputStream();
            try (PrintStream theStream =  new PrintStream(theOS)) {
                e.printStackTrace(theStream);
            }
            WASMLogger.INSTANCE.error(e.getClass().getName() + " : " + new String(theOS.toByteArray()));
        } catch (Exception ex) {
            WASMLogger.INSTANCE.error("General error : " +e.getMessage());
        }
    }
}
