package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameLoop;
import de.mirkosertic.gameengine.core.GameLoopFactory;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.PlaySceneStrategy;
import de.mirkosertic.gameengine.network.DefaultNetworkConnector;
import de.mirkosertic.gameengine.physic.DisableDynamicPhysics;
import de.mirkosertic.gameengine.physic.EnableDynamicPhysics;
import de.mirkosertic.gameengine.teavm.*;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.json.JSON;

import java.util.Map;

public class GameEditor {

    private final EditorHTMLCanvasElement canvasElement;
    private final Window window;
    private Game game;
    private PlaySceneStrategy runSceneStrategy;

    private GameObjectInstance draggingInstance;
    private Position draggingMouseWorldPosition;

    public GameEditor(EditorHTMLCanvasElement aCanvas, Window aWindow) {

        canvasElement = aCanvas;
        window = aWindow;

        GameLoopFactory theGameLoopFactory = new GameLoopFactory();

        // Initialize PIXI
        final Renderer theRenderer;
        if (aWindow.getLocation().getFullURL().contains("canvas")) {
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

        AbstractGameRuntimeFactory theRuntimeFactory = new TeaVMGameRuntimeFactory(
                !aWindow.getLocation().getFullURL().contains("nothreading"),
                aWindow.getLocation().getFullURL().contains("profiling")) {

            @Override
            public void loadingFinished(GameScene aLoadesScene) {
                // No Action Manager
            }
        };

        TeaVMGameSceneLoader theSceneLoader = new TeaVMGameSceneLoader(new TeaVMGameSceneLoader.GameSceneLoadedListener() {
            @Override
            public void onGameSceneLoaded(GameScene aScene) {
                playScene(aScene);
            }

            @Override
            public void onGameSceneLoadedError(Throwable aError) {
                TeaVMLogger.error("Failed to load scene : " + aError.getMessage());
            }
        }, theRuntimeFactory);

        new TeaVMGameLoader(new TeaVMGameLoader.GameLoadedListener() {
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
                        return new Size(aCanvas.getClientWidth(), aCanvas.getClientHeight());
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

                String theSceneId = aGame.defaultSceneProperty().get();

                TeaVMLogger.info("Loading scene " + theSceneId);
                theSceneLoader.loadFromServer(game, theSceneId, new TeaVMGameResourceLoader(theSceneId));
            }

            @Override
            public void onGameLoadedError(Throwable aError) {
                TeaVMLogger.error("Failed to load scene : " + aError);
            }
        }).loadFromServer();

        aCanvas.addEventListener("click", evt -> onMouseClick((TeaVMMouseEvent) evt));
        aCanvas.addEventListener("mousedown", evt -> onMousePressed((TeaVMMouseEvent) evt));
        aCanvas.addEventListener("mousemove", evt -> {
            TeaVMMouseEvent aEvent = (TeaVMMouseEvent) evt;
            if (aEvent.getWhich() != 0) {
                onMouseDragged(aEvent);
            }
        });
        aCanvas.addEventListener("mouseup", evt -> onMouseReleased((TeaVMMouseEvent) evt));
        aWindow.getDocument().getElementById("previewbutton").addEventListener("click", evt -> onPreview());
    }

    public void handleResize() {
        if (runSceneStrategy.hasGameLoop()) {
            runSceneStrategy.handleResize();
        }
    }

    protected void playScene(GameScene aGameScene) {
        runSceneStrategy.playScene(aGameScene);
        runSingleStep(runSceneStrategy.getRunningGameLoop());
    }

    private void onPreview() {

        JSObject theJSForm = TeaVMMap.toJS(runSceneStrategy.getRunningGameLoop().getScene().serialize());
        String theJSON = JSON.stringify(theJSForm);

        window.getLocalStorage().setItem("previewscene", theJSON);

        window.open("preview.html", "_blank");
    }

    private void onMouseClick(TeaVMMouseEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            CameraBehavior theCamera = getCameraBehavior();
            GameObjectInstance[] theInstances = theCamera.findInstancesAt(relativePosition(aEvent));
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
        return new Position(aEvent.getClientX() - canvasElement.getOffsetLeft(), aEvent.getClientY() - canvasElement.getOffsetTop());
    }

    private void onMousePressed(TeaVMMouseEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            CameraBehavior theCamera = getCameraBehavior();
            Position theScreenPosition = relativePosition(aEvent);
            Position theWorldPosition = theCamera.transformFromScreen(theScreenPosition);
            GameObjectInstance[] theFoundInstances = theCamera.findInstancesAt(theScreenPosition);

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


    private void runSingleStep(final GameLoop aGameLoop) {
        if (!aGameLoop.isShutdown()) {
            aGameLoop.singleRunOnlyUpdateGameView();
            TeaVMWindow.requestAnimationFrame(aTimeDelta -> runSingleStep(aGameLoop));
        }
    }
}
