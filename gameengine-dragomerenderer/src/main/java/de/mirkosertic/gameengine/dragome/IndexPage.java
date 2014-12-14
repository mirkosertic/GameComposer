package de.mirkosertic.gameengine.dragome;

import com.dragome.model.DefaultVisualActivity;
import com.dragome.services.ServiceLocator;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Size;

import com.dragome.annotations.PageAlias;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.KeyboardEvent;

@PageAlias(alias= "index")
public class IndexPage extends DefaultVisualActivity {

    private GameRuntime runningRuntime;
    private GameLoop runningGameLoop;
    private GameLoopFactory gameLoopFactory;
    private DragomeRuntimeFactory runtimeFactory;
    private DragomeGameSceneLoader sceneLoader;
    private DragomeGameView gameView;
    private Window window;

    public void build() {

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
                String theSceneId = aGame.defaultSceneProperty().get();
                DragomeLogger.info("Loading game scene " + theSceneId);
                sceneLoader.loadFromServer(aGame.defaultSceneProperty().get(), new DragomeGameResourceLoader(theSceneId));
            }

            @Override
            public void onGameLoadedError() {
                DragomeLogger.error("Failed to load game.");
            }
        });
        DragomeLogger.info("Loading game...");
        theLoader.loadFromServer();

        EventListener theGlobalEventListener = new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                if (aEvent instanceof KeyboardEvent) {
                    handleSingleKeyboardEvent((KeyboardEvent) aEvent);
                }
            }
        };

        window.onResize(new Runnable() {
            @Override
            public void run() {
                if (runningGameLoop != null) {
                    Size theSize = new Size(window.getClientWidth(), window.getClientHeight());
                    runningRuntime.getEventManager().fire(new SetScreenResolution(theSize));
                    gameView.setSize(theSize);
                }
            }
        });

        window.addEventListener(theGlobalEventListener, "keydown", "keypress", "keyup");
    }

    private void handleSingleKeyboardEvent(KeyboardEvent aKeyboardEvent) {
        if (runningGameLoop != null) {
            GameKeyCode theKeyCode = DragomeKeyCodeTranslator.translate(aKeyboardEvent.getKeyIdentifier());
            if ("keyup".equals(aKeyboardEvent.getType())) {
                runningGameLoop.getHumanGameView().getGestureDetector().keyReleased(theKeyCode);
            }
            if ("keydown".equals(aKeyboardEvent.getType())) {
                runningGameLoop.getHumanGameView().getGestureDetector().keyPressed(theKeyCode);
            }
        }
        DragomeLogger.info("KeyEvent " + aKeyboardEvent.getType()+" "+ aKeyboardEvent.getKeyIdentifier());
    }

    private void runSingleStep(final GameLoop aGameLoop) {
        if (!aGameLoop.isShutdown()) {
            aGameLoop.singleRun();
            window.requestAnimationFrame(new Runnable() {
                @Override
                public void run() {
                    runSingleStep(aGameLoop);
                }
            });
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
                sceneLoader.loadFromServer(theSceneId, new DragomeGameResourceLoader(theSceneId));
            }
        });

        GestureDetector theGestureDetector = new DefaultGestureDetector(theEventManager);

        DragomeCanvas theCanvas = new DragomeCanvas(ServiceLocator.getInstance().getDomHandler().getElementBySelector("#html5canvas"));

        if (gameView == null) {
            gameView = new DragomeGameView(theRuntime, theCameraComponent, theGestureDetector, theCanvas);
        } else {
            gameView.prepareNewScene(theRuntime, theCameraComponent, theGestureDetector);
        }

        gameView.setSize(new Size(window.getClientWidth(), window.getClientHeight()));
        theEventManager.fire(new SetScreenResolution(new Size(window.getClientWidth(), window.getClientHeight())));

        runningGameLoop = gameLoopFactory.create(aGameScene, gameView, theRuntime);

        theCameraComponent.initializeFor(aGameScene, thePlayerInstance);

        runSingleStep(runningGameLoop);

        DragomeLogger.info("Scene initialized");

        runningRuntime = theRuntime;
    }
}