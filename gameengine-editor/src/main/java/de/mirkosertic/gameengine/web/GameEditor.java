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
package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.network.DefaultNetworkConnector;
import de.mirkosertic.gameengine.physic.DisableDynamicPhysics;
import de.mirkosertic.gameengine.physic.EnableDynamicPhysics;
import de.mirkosertic.gameengine.teavm.TeaVMDragEvent;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameRuntimeFactory;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameView;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.teavm.TeaVMMap;
import de.mirkosertic.gameengine.teavm.TeaVMMouseEvent;
import de.mirkosertic.gameengine.teavm.TeaVMWindow;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.json.JSON;

public class GameEditor {

    private final SceneEditorHTMLElement sceneEditorHTMLElement;
    private final Window window;
    private Game game;
    private PlaySceneStrategy runSceneStrategy;

    private GameObjectInstance draggingInstance;
    private Position draggingMouseWorldPosition;

    private GameObjectInstance dndCreateInstance;

    private final EditorProject project;

    public GameEditor(SceneEditorHTMLElement aSceneEditor, Window aWindow, EditorProject aEditorProject) {

        sceneEditorHTMLElement = aSceneEditor;
        window = aWindow;
        project = aEditorProject;

        GameLoopFactory theGameLoopFactory = new GameLoopFactory();

        // Initialize PIXI
        final Renderer theRenderer;
        if (aWindow.getLocation().getFullURL().contains("canvas")) {
            theRenderer = Renderer.canvasRenderer(320, 200, aSceneEditor.currentCanvas());
        } else {
            theRenderer = Renderer.autodetectRenderer(320, 200, aSceneEditor.currentCanvas());
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

        AbstractGameRuntimeFactory theRuntimeFactory = new TeaVMGameRuntimeFactory(
                !aWindow.getLocation().getFullURL().contains("nothreading"),
                aWindow.getLocation().getFullURL().contains("profiling")) {

            @Override
            public void loadingFinished(GameScene aLoadesScene) {
                // No Action Manager
            }
        };

        TeaVMGameSceneLoader theSceneLoader = project.createSceneLoader(new TeaVMGameSceneLoader.GameSceneLoadedListener() {
            @Override
            public void onGameSceneLoaded(GameScene aScene) {
                playScene(aScene);
            }

            @Override
            public void onGameSceneLoadedError(Throwable aError) {
                TeaVMLogger.error("Failed to load scene : " + aError.getMessage());
            }
        }, theRuntimeFactory);


        TeaVMGameLoader theGameLoader = project.createGameLoader(new TeaVMGameLoader.GameLoadedListener() {
            @Override
            public void onGameLoaded(Game aGame) {

                game = aGame;

                runSceneStrategy = new PlaySceneStrategy(theRuntimeFactory, theGameLoopFactory, new DefaultNetworkConnector()) {

                    private TeaVMGameView gameView;

                    @Override
                    protected void loadOtherScene(String aSceneId) {
                    }

                    @Override
                    protected Size getScreenSize() {
                        return new Size(aSceneEditor.currentCanvas().getClientWidth(), aSceneEditor.currentCanvas().getClientHeight());
                    }

                    @Override
                    protected GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector) {
                        if (gameView == null) {
                            gameView = new GameEditorGameView(aGameRuntime, aCamera, aGestureDetector, theRenderer);
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

                String theSceneId = aGame.defaultSceneProperty().get();

                TeaVMLogger.info("Loading scene " + theSceneId);
                theSceneLoader.loadFromServer(game, theSceneId, project.createResourceLoaderFor(theSceneId));
            }

            @Override
            public void onGameLoadedError(Throwable aError) {
                TeaVMLogger.error("Failed to load scene : " + aError);
            }
        });

        theGameLoader.loadFromServer();

        EditorHTMLCanvasElement theCanvas = aSceneEditor.currentCanvas();
        theCanvas.addEventListener("click", evt -> onMouseClick((TeaVMMouseEvent) evt));
        theCanvas.addEventListener("mousedown", evt -> onMousePressed((TeaVMMouseEvent) evt));
        theCanvas.addEventListener("mousemove", evt -> {
            TeaVMMouseEvent aEvent = (TeaVMMouseEvent) evt;
            if (aEvent.getWhich() != 0) {
                onMouseDragged(aEvent);
            }
        });
        theCanvas.addEventListener("mouseup", evt -> onMouseReleased((TeaVMMouseEvent) evt));
        theCanvas.addEventListener("dragover", new EventListener<TeaVMDragEvent>() {
            @Override
            public void handleEvent(TeaVMDragEvent aEvent) {
                onDragOver(aEvent);
            }
        });
        theCanvas.addEventListener("dragenter", new EventListener<TeaVMDragEvent>() {
            @Override
            public void handleEvent(TeaVMDragEvent aEvent) {
                onDragEntered(aEvent);
            }
        });
        theCanvas.addEventListener("dragleave", new EventListener<TeaVMDragEvent>() {
            @Override
            public void handleEvent(TeaVMDragEvent aEvent) {
                onDragLeave(aEvent);
            }
        });
        theCanvas.addEventListener("drop", new EventListener<TeaVMDragEvent>() {
            @Override
            public void handleEvent(TeaVMDragEvent aEvent) {
                onDragDropped(aEvent);
            }
        });
        sceneEditorHTMLElement.addEventListener("preview", evt -> onPreview());
    }

    public void handleResize() {
        if (runSceneStrategy.hasGameLoop()) {
            runSceneStrategy.handleResize();
        }
    }

    protected void playScene(GameScene aGameScene) {
        runSceneStrategy.playScene(aGameScene, () -> runSingleStep(runSceneStrategy.getRunningGameLoop()));
    }

    private void onPreview() {

        JSObject theJSForm = TeaVMMap.toJS(runSceneStrategy.getRunningGameLoop().getScene().serialize());
        String theJSON = JSON.stringify(theJSForm);

        project.setCurrentPreview(theJSON);

        window.open("preview.html", "_blank");
    }

    private void onMouseClick(TeaVMMouseEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            CameraBehavior theCamera = getCameraBehavior();
            GameObjectInstance[] theInstances = theCamera.findInstancesAt(relativePosition(aEvent), false);
            if (theInstances.length == 1) {
                setSelectedInstance(theInstances[0]);
            }
        }
    }

    protected void setSelectedInstance(GameObjectInstance aInstance) {
    }

    private CameraBehavior getCameraBehavior() {
        TeaVMGameView theGameView = (TeaVMGameView) runSceneStrategy.getRunningGameLoop().getHumanGameView();
        return theGameView.getCameraBehavior();
    }

    private Position relativePosition(TeaVMMouseEvent aEvent) {
        return new Position(aEvent.getClientX() - sceneEditorHTMLElement.currentCanvas().getOffsetLeft(), aEvent.getClientY() - sceneEditorHTMLElement.currentCanvas().getOffsetTop());
    }

    private void onMousePressed(TeaVMMouseEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            CameraBehavior theCamera = getCameraBehavior();
            Position theScreenPosition = relativePosition(aEvent);
            Position theWorldPosition = theCamera.transformFromScreen(theScreenPosition);
            GameObjectInstance[] theFoundInstances = theCamera.findInstancesAt(theScreenPosition, false);

            if (theFoundInstances.length == 1) {
                draggingInstance = theFoundInstances[0];
                draggingMouseWorldPosition = theWorldPosition;
                runSceneStrategy.getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new DisableDynamicPhysics(draggingInstance));
                setSelectedInstance(draggingInstance);
            } else {
                draggingInstance = null;
                draggingMouseWorldPosition = theScreenPosition;
            }
        }
    }

    private void onMouseDragged(TeaVMMouseEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            CameraBehavior theCamera = getCameraBehavior();
            if (draggingMouseWorldPosition != null) {
                if (draggingInstance != null) {
                    // Move object instance

                    Position theScreenPosition = relativePosition(aEvent);
                    Position theWorldPosition = theCamera.transformFromScreen(theScreenPosition);
                    float theDX = theWorldPosition.x - draggingMouseWorldPosition.x;
                    float theDY = theWorldPosition.y - draggingMouseWorldPosition.y;

                    Position theObjectPosition = draggingInstance.positionProperty().get();
                    draggingInstance.positionProperty()
                            .set(new Position(theObjectPosition.x + theDX, theObjectPosition.y + theDY));

                    draggingMouseWorldPosition = theWorldPosition;
                } else {

                    Position theScreenPosition = relativePosition(aEvent);

                    float theDX = theScreenPosition.x - draggingMouseWorldPosition.x;
                    float theDY = theScreenPosition.y - draggingMouseWorldPosition.y;

                    // Move camera
                    Position theObjectPosition = theCamera.getInstance().positionProperty().get();
                    theCamera.getInstance().positionProperty()
                            .set(new Position(theObjectPosition.x - theDX, theObjectPosition.y - theDY));

                    draggingMouseWorldPosition = theScreenPosition;
                }
            }
        }
    }

    private void onMouseReleased(TeaVMMouseEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {

            if (draggingInstance != null) {
                runSceneStrategy.getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new EnableDynamicPhysics(draggingInstance));
            }
            draggingMouseWorldPosition = null;
            draggingInstance = null;
        }
    }

    private void onDragOver(TeaVMDragEvent aEvent) {
        if (dndCreateInstance != null) {
            Position theNewPosition = getCameraBehavior().transformFromScreen(new Position(aEvent.getClientX() - sceneEditorHTMLElement.currentCanvas().getOffsetLeft(), aEvent.getClientY() - sceneEditorHTMLElement.currentCanvas().getOffsetTop()));
            dndCreateInstance.positionProperty().set(theNewPosition);
            aEvent.preventDefault();
        }
    }

    private void onDragEntered(TeaVMDragEvent aEvent) {
        String theID = window.getLocalStorage().getItem(Constants.DND_OBJECT_ID);
        if (theID != null && theID.length() > 0) {
            GameScene theScene = runSceneStrategy.getRunningGameLoop().getScene();
            GameObject theGameObject = theScene.findObjectByID(theID);
            GameObjectInstance theInstance = theScene.createFrom(theGameObject);
            theInstance.positionProperty().set(getCameraBehavior().transformFromScreen(new Position(aEvent.getClientX() - sceneEditorHTMLElement.currentCanvas().getOffsetLeft(), aEvent.getClientY() - sceneEditorHTMLElement.currentCanvas().getOffsetTop())));

            theScene.addInstance(theInstance);
            theScene.getRuntime().getEventManager().fire(new DisableDynamicPhysics(theInstance));

            dndCreateInstance = theInstance;
            aEvent.preventDefault();
        }
    }

    private void onDragLeave(TeaVMDragEvent aEvent) {
        if (dndCreateInstance != null) {
            runSceneStrategy.getRunningGameLoop().getScene().removeGameObjectInstance(dndCreateInstance);
            dndCreateInstance = null;
            aEvent.preventDefault();
        }
    }

    private void onDragDropped(TeaVMDragEvent aEvent) {
        if (dndCreateInstance != null) {
            aEvent.preventDefault();
            //if (snapToGrid.isSelected()) {
              //  dndCreateInstance.positionProperty().set(gameView.snapToGrid(dndCreateInstance.positionProperty().get()));
            //}

            runSceneStrategy.getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new EnableDynamicPhysics(dndCreateInstance));
            dndCreateInstance = null;
        }
    }

    private void runSingleStep(final GameLoop aGameLoop) {
        if (!aGameLoop.isShutdown()) {
            aGameLoop.singleRunOnlyUpdateGameView();
            TeaVMWindow.requestAnimationFrame(aTimeDelta -> runSingleStep(aGameLoop));
        }
    }
}
