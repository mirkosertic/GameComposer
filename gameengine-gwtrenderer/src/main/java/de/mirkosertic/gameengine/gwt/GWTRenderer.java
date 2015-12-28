package de.mirkosertic.gameengine.gwt;

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
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.network.NetworkConnector;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.TouchIdentifier;
import de.mirkosertic.gameengine.type.TouchPosition;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;

import java.util.logging.Level;
import java.util.logging.Logger;
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
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
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

public class GWTRenderer implements EntryPoint {

    private final static Logger LOGGER = Logger.getLogger(GWTRenderer.class.getName());

    private static final String holderId = "canvasholder";
    private static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";

    private Canvas canvas;
    private Canvas overlayCanvas;
    private GWTGameRuntimeFactory runtimeFactory;
    private GWTGameSceneLoader sceneLoader;
    private Game game;
    private GameLoopFactory gameLoopFactory;
    private PlaySceneStrategy playSceneStrategy;
    private NetworkConnector networkConnector;

    @Override
    public void onModuleLoad() {

        networkConnector = new GWTNetworkConnector();

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
                sceneLoader.loadFromServer(aGame, aGame.defaultSceneProperty().get(), new GWTGameResourceLoader(aGame.defaultSceneProperty().get()));
            }

            @Override
            public void onGameLoadedError(Throwable aThrowable) {
                LOGGER.log(Level.SEVERE, "Error loading game", aThrowable);
            }
        });

        playSceneStrategy = new PlaySceneStrategy(runtimeFactory, gameLoopFactory, networkConnector) {

            private GenericAbstractGameView gameView;

            @Override
            protected void loadOtherScene(String aSceneId) {
                sceneLoader.loadFromServer(game, aSceneId, new GWTGameResourceLoader(aSceneId));
            }

            @Override
            protected Size getScreenSize() {
                return new Size(Window.getClientWidth(), Window.getClientHeight());
            }

            @Override
            protected GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector) {
                if (game.enableWebGLProperty().get()) {
                    WebGLRenderingContext theWebGLContext = (WebGLRenderingContext) canvas.getContext("webgl");
                    if (theWebGLContext == null) {
                        theWebGLContext = (WebGLRenderingContext) canvas.getContext("experimental-webgl");
                    }
                    if (theWebGLContext != null) {
                        // WebGL is supported
                        gameView = new GWTWebGLGameView(aGameRuntime, theWebGLContext, overlayCanvas, aCamera, aGestureDetector);
                    } else {
                        // Fallback to canvas
                        gameView = new GWTCanvasGameView(aGameRuntime, canvas, aCamera, aGestureDetector);
                    }
                } else {
                    gameView = new GWTCanvasGameView(aGameRuntime, canvas, aCamera, aGestureDetector);
                }

                gameView.setCurrentScreenSize(getScreenSize());

                return gameView;
            }

            @Override
            public void handleResize() {
                Size theSize = getScreenSize();
                if (hasGameLoop()) {
                    getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new SetScreenResolution(theSize));
                    gameView.setCurrentScreenSize(theSize);
                }

                resizeCanvas(theSize.width, theSize.height);
            }
        };

        theLoader.loadFromServer();

        canvas.setStyleName("mainCanvas");
        RootPanel.get(holderId).add(canvas);

        overlayCanvas.setStyleName("overlayCanvas");
        RootPanel.get(holderId).add(overlayCanvas);

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                if (playSceneStrategy.hasGameLoop()) {
                    playSceneStrategy.handleResize();
                }
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
        RootPanel.get().addHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent aEvent) {
                handleMouseDown(aEvent);
            }
        }, MouseDownEvent.getType());
        RootPanel.get().addHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent aEvent) {
                handleMouseUp(aEvent);
            }
        }, MouseUpEvent.getType());

        // This must be done or no events are fired at all
        RootPanel.get().sinkEvents(Event.KEYEVENTS);
        RootPanel.get().sinkEvents(Event.TOUCHEVENTS);
        RootPanel.get().sinkEvents(Event.MOUSEEVENTS);
    }

    private void handleMouseUp(MouseUpEvent aEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mouseReleased(
                    new Position(aEvent.getClientX(), aEvent.getClientY())
            );
        }
    }

    private void handleMouseDown(MouseDownEvent aEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mousePressed(
                    new Position(aEvent.getClientX(), aEvent.getClientY())
            );
        }
    }

    private void handleOnKeyUpEvent(KeyUpEvent aEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            GameKeyCode theCode = GWTKeyCodeTranslator.translate(aEvent.getNativeKeyCode());
            if (theCode != null) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyReleased(theCode);
            }
        }
    }

    private void handleOnKeyDownEvent(KeyDownEvent aEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            GameKeyCode theCode = GWTKeyCodeTranslator.translate(aEvent.getNativeKeyCode());
            if (theCode != null) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyPressed(theCode);
            }
        }
    }

    private void handleOnKeyPressedEvent(KeyPressEvent aEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            GameKeyCode theCode = GameKeyCode.fromChar(aEvent.getCharCode());
            if (theCode != null) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyPressed(theCode);
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
        if (playSceneStrategy.hasGameLoop()) {
            GestureDetector theDetector = playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchStarted(toArray(aEvent.getTouches()));
        }
    }

    private void handleOnTouchEnd(TouchEndEvent aEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            GestureDetector theDetector = playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchEnded(toArray(aEvent.getTouches()));
        }
    }

    private void handleOnTouchMoved(TouchMoveEvent aEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            GestureDetector theDetector = playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchMoved(toArray(aEvent.getTouches()));
        }
    }

    private void handleOnTouchCanceled(TouchCancelEvent aEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            GestureDetector theDetector = playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchCanceled(toArray(aEvent.getTouches()));
        }
    }

    private void playScene(GameScene aGameScene) {

        playSceneStrategy.playScene(aGameScene);
        canvas.setFocus(true);

        final GameLoop theCurrentLoop = playSceneStrategy.getRunningGameLoop();

        AnimationScheduler.get().requestAnimationFrame(new AnimationScheduler.AnimationCallback() {

            @Override
            public void execute(double v) {
                theCurrentLoop.singleRun();
                // Request another animation
                if (!theCurrentLoop.isShutdown()) {
                    AnimationScheduler.get().requestAnimationFrame(this);
                }
            }
        });
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