package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.*;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Size;
import org.teavm.dom.browser.TimerHandler;
import org.teavm.dom.events.Event;
import org.teavm.dom.events.EventListener;
import org.teavm.dom.events.EventTarget;
import org.teavm.dom.html.HTMLDocument;
import org.teavm.jso.JS;

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

    private TeaVMRenderer() {
    }

    void boot() {

        TeaVMLogger.info("Booting game runtime");

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
                sceneLoader.loadFromServer(theSceneId, new TeaVMGameResourceLoader(theSceneId));
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

/*        window.onResize(new Runnable() {
            @Override
            public void run() {
                if (runningGameLoop != null) {
                    Size theSize = new Size(window.getInnerWidth(), window.getInnerHeoght());
                    runningRuntime.getEventManager().fire(new SetScreenResolution(theSize));
                    gameView.setSize(theSize);
                }
            }
        });*/
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

    private void runSingleStep(final GameLoop aGameLoop) {
        if (!aGameLoop.isShutdown()) {
            aGameLoop.singleRun();
            window.requestAnimationFrame(new TeaVMWindow.RenderFrameHandler() {
                @Override
                public void renderFrame(int aTimeDelta) {
                    runSingleStep(aGameLoop);
                }
            });
/*            window.setTimeout(new TimerHandler() {
                @Override
                public void onTimer() {
                    runSingleStep(aGameLoop);
                }
            }, 16);*/
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
        CameraBehavior theCameraComponent = theCameraObjectInstance.getComponent(CameraBehavior.class);

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
                sceneLoader.loadFromServer(theSceneId, new TeaVMGameResourceLoader(theSceneId));
            }
        });

        GestureDetector theGestureDetector = new DefaultGestureDetector(theEventManager);

        if (gameView == null) {
            gameView = new TeaVMGameView(theRuntime, theCameraComponent, theGestureDetector, document,
                    document.getElementById("canvas"));
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