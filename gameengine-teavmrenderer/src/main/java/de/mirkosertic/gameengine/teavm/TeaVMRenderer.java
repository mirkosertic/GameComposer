package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameLoop;
import de.mirkosertic.gameengine.core.GameLoopFactory;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.RunScene;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.TouchIdentifier;
import de.mirkosertic.gameengine.type.TouchPosition;

import org.teavm.dom.events.Event;
import org.teavm.dom.events.EventListener;
import org.teavm.dom.events.EventTarget;
import org.teavm.dom.html.HTMLCanvasElement;
import org.teavm.dom.html.HTMLDocument;
import org.teavm.dom.html.HTMLElement;
import org.teavm.jso.JS;
import org.teavm.jso.JSArrayReader;

public class TeaVMRenderer {

    private static final TeaVMWindow window = (TeaVMWindow) JS.getGlobal();
    private static final HTMLDocument document = window.getDocument();

    public static void main(String[] args) {
        TeaVMRenderer theRenderer = new TeaVMRenderer();
        theRenderer.boot();
    }

    private GameRuntime runningRuntime;
    private GameLoop runningGameLoop;
    private GameLoopFactory gameLoopFactory;
    private TeaVMGameRuntimeFactory runtimeFactory;
    private TeaVMGameSceneLoader sceneLoader;
    private TeaVMGameView gameView;
    private HTMLCanvasElement canvasElement;
    private HTMLElement resourceCache;

    private TeaVMRenderer() {
    }

    void boot() {

        TeaVMLogger.info("Booting game runtime");

        canvasElement = (HTMLCanvasElement) document.getElementById("html5canvas");
        resourceCache = document.getElementById("resourcecache");

        gameLoopFactory = new GameLoopFactory();
        runtimeFactory = new TeaVMGameRuntimeFactory();

        sceneLoader = new TeaVMGameSceneLoader(new TeaVMGameSceneLoader.GameSceneLoadedListener() {
            @Override
            public void onGameSceneLoaded(GameScene aScene) {
                playScene(aScene);
            }

            @Override
            public void onGameSceneLoadedError(Throwable aError) {
                TeaVMLogger.error("Failed to load scene : " + aError.getMessage());
            }
        }, runtimeFactory, window);

        new TeaVMGameLoader(new TeaVMGameLoader.GameLoadedListener() {
            @Override
            public void onGameLoaded(Game aGame) {
                String theSceneId = aGame.defaultSceneProperty().get();
                TeaVMLogger.info("Loading scene " + theSceneId);
                sceneLoader.loadFromServer(theSceneId, new TeaVMGameResourceLoader(theSceneId, document, resourceCache));
            }

            @Override
            public void onGameLoadedError(Throwable aError) {
                TeaVMLogger.error("Failed to load scene : " + aError);
            }
        }, window).loadFromServer();

        EventTarget documentEventTarget = (EventTarget)document;
        documentEventTarget.addEventListener("keydown", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                keyPressed((TeaVMKeyEvent) aEvent);
            }
        }, false);
        documentEventTarget.addEventListener("keyup", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                keyReleased((TeaVMKeyEvent) aEvent);
            }
        }, false);
        canvasElement.addEventListener("touchstart", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                touchStarted((TeaVMTouchEvent) aEvent);
            }
        }, false);
        canvasElement.addEventListener("touchend", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                touchEnded((TeaVMTouchEvent) aEvent);
            }
        }, false);
        canvasElement.addEventListener("touchcancel", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                touchCanceled((TeaVMTouchEvent) aEvent);
            }
        }, false);
        canvasElement.addEventListener("touchmove", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                touchMoved((TeaVMTouchEvent) aEvent);
            }
        }, false);

        ((EventTarget) window).addEventListener("resize", new EventListener() {
            @Override
            public void handleEvent(Event evt) {
                if (runningGameLoop != null) {
                    Size theSize = new Size(window.getInnerWidth(), window.getInnerHeight());
                    runningRuntime.getEventManager().fire(new SetScreenResolution(theSize));
                    gameView.setSize(theSize);
                }
            }
        }, true);
    }

    private void keyPressed(TeaVMKeyEvent aEvent) {
        if (runningGameLoop != null) {
            int theCode = JS.isUndefined(aEvent.getWhich()) ? JS.unwrapInt(aEvent.getWhich()) : aEvent.getKeyCode();
            GameKeyCode theKeyCode = TeaVMKeyCodeTranslator.translate(theCode);
            runningGameLoop.getHumanGameView().getGestureDetector().keyPressed(theKeyCode);
            TeaVMLogger.info("KeyEvent keyPressed " + theCode);
        }
    }

    private void keyReleased(TeaVMKeyEvent aEvent) {
        if (runningGameLoop != null) {
            int theCode = JS.isUndefined(aEvent.getWhich()) ? JS.unwrapInt(aEvent.getWhich()) : aEvent.getKeyCode();
            GameKeyCode theKeyCode = TeaVMKeyCodeTranslator.translate(theCode);
            runningGameLoop.getHumanGameView().getGestureDetector().keyReleased(theKeyCode);
            TeaVMLogger.info("KeyEvent keyReleased " + theCode);
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
        if (runningGameLoop != null) {
            GestureDetector theDetector = runningGameLoop.getHumanGameView().getGestureDetector();
            theDetector.touchStarted(toArray(aEvent.getTouches()));
        }
    }

    private void touchEnded(TeaVMTouchEvent aEvent) {
        if (runningGameLoop != null) {
            GestureDetector theDetector = runningGameLoop.getHumanGameView().getGestureDetector();
            theDetector.touchEnded(toArray(aEvent.getTouches()));
        }
    }

    private void touchMoved(TeaVMTouchEvent aEvent) {
        if (runningGameLoop != null) {
            GestureDetector theDetector = runningGameLoop.getHumanGameView().getGestureDetector();
            theDetector.touchMoved(toArray(aEvent.getTouches()));
        }
    }

    private void touchCanceled(TeaVMTouchEvent aEvent) {
        if (runningGameLoop != null) {
            GestureDetector theDetector = runningGameLoop.getHumanGameView().getGestureDetector();
            theDetector.touchCanceled(toArray(aEvent.getTouches()));
        }
    }

    private void runSingleStep(final GameLoop aGameLoop) {
        if (!aGameLoop.isShutdown()) {
            aGameLoop.singleRun();
            window.requestAnimationFrame(new TeaVMWindow.RenderFrameHandler() {
                @Override
                public void renderFrame(int aTimeDelta) {
                    runSingleStep(aGameLoop);
                }
            });
        }
    }

    private void playScene(GameScene aGameScene) {
        if (runningGameLoop != null) {
            runningGameLoop.shutdown();
        }

        GameEventManager theEventManager = aGameScene.getRuntime().getEventManager();
        GameRuntime theRuntime = aGameScene.getRuntime();

        runtimeFactory.loadingFinished(aGameScene);

        // Detect and create a camera
        GameObject theCameraObject = aGameScene.cameraObjectProperty().get();
        GameObjectInstance theCameraObjectInstance = aGameScene.createFrom(theCameraObject);
        CameraBehavior theCameraComponent = theCameraObjectInstance.getBehavior(CameraBehavior.class);

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
                String theSceneId = aEvent.sceneId;
                sceneLoader.loadFromServer(theSceneId, new TeaVMGameResourceLoader(theSceneId, document, resourceCache));
            }
        });

        GestureDetector theGestureDetector = new DefaultGestureDetector(theEventManager);

        if (gameView == null) {
            gameView = new TeaVMGameView(theRuntime, theCameraComponent, theGestureDetector, canvasElement);
        } else {
            gameView.prepareNewScene(theRuntime, theCameraComponent, theGestureDetector);
        }

        Size theSize = new Size(window.getInnerWidth(), window.getInnerHeight());
        TeaVMLogger.info("Size is " + theSize.width + " " + theSize.height);
        gameView.setSize(theSize);
        theEventManager.fire(new SetScreenResolution(theSize));

        runningGameLoop = gameLoopFactory.create(aGameScene, gameView, theRuntime);

        theCameraComponent.initializeFor(aGameScene, thePlayerInstance);

        runSingleStep(runningGameLoop);
        runningRuntime = theRuntime;
    }
}