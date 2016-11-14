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
package de.mirkosertic.gameengine.dragome;

import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.KeyboardEvent;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.html.HTMLCanvasElement;

import com.dragome.services.WebServiceLocator;
import com.dragome.view.DefaultVisualActivity;
import com.dragome.web.annotations.PageAlias;
import com.dragome.web.enhancers.jsdelegate.JsCast;

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
import de.mirkosertic.gameengine.event.SystemException;
import de.mirkosertic.gameengine.network.NetworkConnector;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

@PageAlias(alias= "index")
public class IndexPage extends DefaultVisualActivity {

    private GameLoopFactory gameLoopFactory;
    private DragomeRuntimeFactory runtimeFactory;
    private DragomeGameSceneLoader sceneLoader;
    private Window window;
    private PlaySceneStrategy playSceneStrategy;
    private Game game;
    private NetworkConnector networkConnector;

    @Override
    public void build() {

        networkConnector = new DragomeNetworkConnector();

        window = new Window();

        gameLoopFactory = new GameLoopFactory();
        runtimeFactory = new DragomeRuntimeFactory();

        sceneLoader = new DragomeGameSceneLoader(new DragomeGameSceneLoader.GameSceneLoadedListener() {
            @Override
            public void onGameSceneLoaded(GameScene aScene) {
                DragomeLogger.info("Loaded game scene " + aScene.nameProperty().get());
                playScene(aScene);
            }

            @Override
            public void onGameSceneLoadedError() {
                DragomeLogger.error("Failed to load scene");
            }
        }, runtimeFactory);

        DragomeGameLoader theLoader = new DragomeGameLoader(new DragomeGameLoader.GameLoadedListener() {
            @Override
            public void onGameLoaded(Game aGame) {
                game = aGame;
                String theSceneId = aGame.defaultSceneProperty().get();
                DragomeLogger.info("Loading game scene " + theSceneId);
                sceneLoader.loadFromServer(aGame, aGame.defaultSceneProperty().get(), new DragomeGameResourceLoader(theSceneId));
            }

            @Override
            public void onGameLoadedError() {
                DragomeLogger.error("Failed to load game.");
            }
        });

        playSceneStrategy = new PlaySceneStrategy(runtimeFactory, gameLoopFactory, networkConnector) {

            private DragomeGameView gameView;

            @Override
            protected void loadOtherScene(String aSceneId) {
                sceneLoader.loadFromServer(game, aSceneId, new DragomeGameResourceLoader(aSceneId));
            }

            @Override
            protected Size getScreenSize() {
                return new Size(window.getClientWidth(), window.getClientHeight());
            }

            @Override
            protected void handleSystemException(SystemException e) {
                super.handleSystemException(e);
                DragomeLogger.error(e.toString()+" "+e.exception);
            }

            @Override
            protected GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector) {
                if (gameView == null) {
                    HTMLCanvasElement theCanvas= JsCast.castTo(WebServiceLocator.getInstance().getDomHandler().getElementBySelector("#html5canvas"), HTMLCanvasElement.class);
                    gameView = new DragomeGameView(aGameRuntime, aCamera, aGestureDetector, theCanvas);
                } else {
                    gameView.prepareNewScene(aGameRuntime, aCamera, aGestureDetector);
                }
                gameView.setCurrentScreenSize(getScreenSize());
                return gameView;
            }

            @Override
            public void handleResize() {
                Size theSize = getScreenSize();
                getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new SetScreenResolution(theSize));
                gameView.setCurrentScreenSize(theSize);
            }
        };

        DragomeLogger.info("Loading game...");
        theLoader.loadFromServer();

        EventListener theGlobalEventListener = aEvent -> {
            switch (aEvent.getType()) {
                case "keydown":
                case "keypress":
                case "keyup":
                    handleSingleKeyboardEvent(JsCast.castTo(aEvent,KeyboardEvent.class));
                    break;
                case "mousedown":
                case "mouseup":
                    handleSingleMouseEvent(JsCast.castTo(aEvent,MouseEvent.class));
                    break;
            }
        };

        window.onResize(() -> {
            if (playSceneStrategy.hasGameLoop()) {
                playSceneStrategy.handleResize();
            }
        });

        window.addEventListener(theGlobalEventListener, "keydown", "keypress", "keyup", "mousedown", "mouseup");
    }

    private void handleSingleMouseEvent(MouseEvent aMouseEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            if ("mousedown".equals(aMouseEvent.getType())) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mousePressed(
                        new Position(aMouseEvent.getClientX(), aMouseEvent.getClientY())
                );
            }
            if ("mouseup".equals(aMouseEvent.getType())) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mouseReleased(
                        new Position(aMouseEvent.getClientX(), aMouseEvent.getClientY())
                );
            }
        }
    }

    private void handleSingleKeyboardEvent(KeyboardEvent aKeyboardEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            GameKeyCode theKeyCode = DragomeKeyCodeTranslator.translate(Integer.toString(aKeyboardEvent.getKeyCode()));
            if ("keyup".equals(aKeyboardEvent.getType())) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyReleased(theKeyCode);
            }
            if ("keydown".equals(aKeyboardEvent.getType())) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyPressed(theKeyCode);
            }
        }
        DragomeLogger.info("KeyEvent " + aKeyboardEvent.getType()+" "+ aKeyboardEvent.getCharCode());
    }

    private void runSingleStep(final GameLoop aGameLoop) {
        if (!aGameLoop.isShutdown()) {
            aGameLoop.singleRun();
            window.requestAnimationFrame(() -> runSingleStep(aGameLoop));
        }
    }

    private void playScene(GameScene aGameScene) {
        playSceneStrategy.playScene(aGameScene).thenContinue(aResult -> {
            runSingleStep(playSceneStrategy.getRunningGameLoop());

            DragomeLogger.info("Scene initialized");
        });
    }
}