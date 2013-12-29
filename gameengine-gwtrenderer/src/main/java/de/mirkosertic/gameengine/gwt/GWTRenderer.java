package de.mirkosertic.gameengine.gwt;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.camera.FollowCameraProcess;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.types.GameKeyCode;
import de.mirkosertic.gameengine.core.GameLoop;
import de.mirkosertic.gameengine.core.GameLoopFactory;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.RunScene;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.types.TouchIdentifier;
import de.mirkosertic.gameengine.types.TouchPosition;
import de.mirkosertic.gameengine.processes.StartProcess;
import de.mirkosertic.gameengine.types.Size;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GWTRenderer implements EntryPoint {

    private final static Logger LOGGER = Logger.getLogger(GWTRenderer.class.getName());

    private static final String holderId = "canvasholder";
    private static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";

    private Canvas canvas;
    private Canvas overlayCanvas;
    private GWTGameRuntimeFactory runtimeFactory;
    private GWTGameSceneLoader sceneLoader;
    private Game game;
    private GameLoop runningGameLoop;
    private GameScene loadedScene;
    private GameLoopFactory gameLoopFactory;

    @Override
    public void onModuleLoad() {
        canvas = Canvas.createIfSupported();
        if (canvas == null) {
            RootPanel.get(holderId).add(new Label(upgradeMessage));
            return;
        }
        overlayCanvas = Canvas.createIfSupported();
        if (overlayCanvas == null) {
            RootPanel.get(holderId).add(new Label(upgradeMessage));
            return;
        }

        gameLoopFactory = new GameLoopFactory();

        // We need a factory to create new game runtimes for every scene
        runtimeFactory = new GWTGameRuntimeFactory();

        // This is the loader for our scenes
        sceneLoader = new GWTGameSceneLoader(
                new GWTGameSceneLoader.GameSceneLoadedListener() {
                    @Override
                    public void onGameSceneLoaded(GameScene aScene) {
                        LOGGER.info("Game loaded, loading scene " + aScene.nameProperty().get());
                        playScene(aScene);
                    }

                    @Override
                    public void onGameSceneLoadedError(Throwable aThrowable) {
                        LOGGER.log(Level.SEVERE, "Error loading game scene", aThrowable);
                    }
                }, runtimeFactory);

        // The game loader itself
        GWTGameLoader theLoader = new GWTGameLoader(new GWTGameLoader.GameLoadedListener() {
            @Override
            public void onGameLoaded(Game aGame) {
                game = aGame;
                sceneLoader.loadFromServer(aGame.defaultSceneProperty().get(), new GWTGameResourceLoader(aGame.defaultSceneProperty().get()));
            }

            @Override
            public void onGameLoadedError(Throwable aThrowable) {
                LOGGER.log(Level.SEVERE, "Error loading game", aThrowable);
            }
        });
        theLoader.loadFromServer();

        canvas.setStyleName("mainCanvas");
        RootPanel.get(holderId).add(canvas);

        overlayCanvas.setStyleName("overlayCanvas");
        RootPanel.get(holderId).add(overlayCanvas);

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                resizeCanvas(event.getWidth(), event.getHeight());
            }
        });

        resizeCanvas(Window.getClientWidth(), Window.getClientHeight());

        // Keylistener must only be registered once, or strange things will happen
        // They delegate to the event manager of the currently loaded scene
        RootPanel.get().addHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent aEvent) {
                handleOnKeyDownEvent(aEvent);
            }
        }, KeyDownEvent.getType());
        RootPanel.get().addHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent aEvent) {
                handleOnKeyUpEvent(aEvent);
            }
        }, KeyUpEvent.getType());
        RootPanel.get().addHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent aEvent) {
                handleOnKeyPressedEvent(aEvent);
            }
        }, KeyPressEvent.getType());
        RootPanel.get().addHandler(new TouchStartHandler() {
            @Override
            public void onTouchStart(TouchStartEvent aEvent) {
                handleOnTouchStart(aEvent);
            }
        }, TouchStartEvent.getType());
        RootPanel.get().addHandler(new TouchEndHandler() {
            @Override
            public void onTouchEnd(TouchEndEvent aEvent) {
                handleOnTouchEnd(aEvent);
            }
        }, TouchEndEvent.getType());
        RootPanel.get().addHandler(new TouchMoveHandler() {
            @Override
            public void onTouchMove(TouchMoveEvent aEvent) {
                handleOnTouchMoved(aEvent);
            }
        }, TouchMoveEvent.getType());
        RootPanel.get().addHandler(new TouchCancelHandler() {
            @Override
            public void onTouchCancel(TouchCancelEvent aEvent) {
                handleOnTouchCanceled(aEvent);
            }
        }, TouchCancelEvent.getType());

        // This must be done or no events are fired at all
        RootPanel.get().sinkEvents(Event.KEYEVENTS);
    }

    private void handleOnKeyUpEvent(KeyUpEvent aEvent) {
        if (runningGameLoop != null) {
            GameKeyCode theCode = GWTKeyCodeTranslator.translate(aEvent.getNativeKeyCode());
            if (theCode != null) {
                runningGameLoop.getHumanGameView().getGestureDetector().keyReleased(theCode);
            }
        }
    }

    private void handleOnKeyDownEvent(KeyDownEvent aEvent) {
        if (runningGameLoop != null) {
            GameKeyCode theCode = GWTKeyCodeTranslator.translate(aEvent.getNativeKeyCode());
            if (theCode != null) {
                runningGameLoop.getHumanGameView().getGestureDetector().keyPressed(theCode);
            }
        }
    }

    private void handleOnKeyPressedEvent(KeyPressEvent aEvent) {
        if (runningGameLoop != null) {
            GameKeyCode theCode = GameKeyCode.fromChar(aEvent.getCharCode());
            if (theCode != null) {
                runningGameLoop.getHumanGameView().getGestureDetector().keyPressed(theCode);
            }
        }
    }

    private TouchPosition[] toArray(JsArray<Touch> aTouches) {
        TouchPosition[] thePositions = new TouchPosition[aTouches.length()];
        for (int i=0;i<aTouches.length();i++) {
            Touch theTouch = aTouches.get(i);
            thePositions[i] = new TouchPosition(new TouchIdentifier(theTouch.getIdentifier()), theTouch.getScreenX(), theTouch.getScreenY());
        }
        return thePositions;
    }

    private void handleOnTouchStart(TouchStartEvent aEvent) {
        if (runningGameLoop != null) {
            GestureDetector theDetector = runningGameLoop.getHumanGameView().getGestureDetector();
            theDetector.touchStarted(toArray(aEvent.getTouches()));
        }
    }

    private void handleOnTouchEnd(TouchEndEvent aEvent) {
        if (runningGameLoop != null) {
            GestureDetector theDetector = runningGameLoop.getHumanGameView().getGestureDetector();
            theDetector.touchEnded(toArray(aEvent.getTouches()));
        }
    }

    private void handleOnTouchMoved(TouchMoveEvent aEvent) {
        if (runningGameLoop != null) {
            GestureDetector theDetector = runningGameLoop.getHumanGameView().getGestureDetector();
            theDetector.touchMoved(toArray(aEvent.getTouches()));
        }
    }

    private void handleOnTouchCanceled(TouchCancelEvent aEvent) {
        if (runningGameLoop != null) {
            GestureDetector theDetector = runningGameLoop.getHumanGameView().getGestureDetector();
            theDetector.touchCanceled(toArray(aEvent.getTouches()));
        }
    }

    private void playScene(GameScene aGameScene) {

        // Shutdown the game loop for the previous loaded scene
        // This will also shutdown the animation scheduler
        if (runningGameLoop != null) {
            runningGameLoop.shutdown();
        }

        GameEventManager theEventManager = aGameScene.getRuntime().getEventManager();
        GameRuntime theRuntime = aGameScene.getRuntime();

        runtimeFactory.loadingFinished(aGameScene);

        // Detect and create a camera
        GameObject theCameraObject = aGameScene.cameraObjectProperty().get();
        GameObjectInstance theCameraObjectInstance = aGameScene.createFrom(theCameraObject);
        CameraComponent theCameraComponent = theCameraObjectInstance.getComponent(CameraComponent.class);

        GameObjectInstance thePlayerInstance = null;
        for (GameObjectInstance theInstance : aGameScene.getInstances()) {
            if (theInstance.getOwnerGameObject() == aGameScene.defaultPlayerProperty().get()) {
                thePlayerInstance = theInstance;
            }
        }

        // This is our hook to load new scenes
        theEventManager.register(null, RunScene.class, new GameEventListener<RunScene>() {
            @Override
            public void handleGameEvent(RunScene aEvent) {
                String theSceneId = aEvent.sceneIdProperty().get();
                sceneLoader.loadFromServer(theSceneId, new GWTGameResourceLoader(theSceneId));
            }
        });

        AbstractWebGameView theGameView;
        if (game.enableWebGLProperty().get()) {
            WebGLRenderingContext theWebGLContext = (WebGLRenderingContext) canvas.getContext("webgl");
            if (theWebGLContext == null) {
                theWebGLContext = (WebGLRenderingContext) canvas.getContext("experimental-webgl");
            }
            if (theWebGLContext != null) {
                // WebGL is supported
                theGameView = new GWTWebGLGameView(theRuntime, theWebGLContext, overlayCanvas, theCameraComponent);
            } else {
                // Fallback to canvas
                theGameView = new GWTCanvasGameView(theRuntime, canvas, theCameraComponent);
            }
        } else {
            theGameView = new GWTCanvasGameView(theRuntime, canvas, theCameraComponent);
        }

        theGameView.setSize(new Size(Window.getClientWidth(), Window.getClientHeight()));
        theEventManager.fire(new SetScreenResolution(new Size(Window.getClientWidth(), Window.getClientHeight())));

        runningGameLoop = gameLoopFactory.create(aGameScene, theGameView, theRuntime);

        AnimationScheduler.get().requestAnimationFrame(new AnimationScheduler.AnimationCallback() {
            @Override
            public void execute(double v) {
                runningGameLoop.singleRun();
                // Request another animation
                if (!runningGameLoop.isShutdown()) {
                    AnimationScheduler.get().requestAnimationFrame(this);
                }
            }
        });

        switch (theCameraComponent.getTemplate().typeProperty().get()) {
            case FOLLOWPLAYER:
                theCameraComponent.centerOn(thePlayerInstance);
                theEventManager.fire(new StartProcess(new FollowCameraProcess(theCameraObjectInstance, thePlayerInstance)));
                break;
            case CENTERONSCENE:
                break;
        }

        canvas.setFocus(true);

        loadedScene = aGameScene;
    }

    void resizeCanvas(int aWidth, int aHeight) {
        canvas.setWidth(aWidth + "px");
        canvas.setHeight(aHeight + "px");
        canvas.setCoordinateSpaceWidth(aWidth);
        canvas.setCoordinateSpaceHeight(aHeight);

        overlayCanvas.setWidth(aWidth + "px");
        overlayCanvas.setHeight(aHeight + "px");
        overlayCanvas.setCoordinateSpaceWidth(aWidth);
        overlayCanvas.setCoordinateSpaceHeight(aHeight);
    }
}