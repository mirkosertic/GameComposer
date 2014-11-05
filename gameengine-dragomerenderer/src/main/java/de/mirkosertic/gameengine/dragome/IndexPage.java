package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.type.Size;

import com.dragome.annotations.PageAlias;
import com.dragome.debugging.execution.DragomeVisualActivity;
import com.dragome.html.dom.Timer;

@PageAlias(alias= "index")
public class IndexPage extends DragomeVisualActivity {

    private GameLoop runningGameLoop;
    private GameLoopFactory gameLoopFactory;
    private DragomeRuntimeFactory runtimeFactory;
    private DragomeGameSceneLoader sceneLoader;
    private Timer timer = new Timer();

    public void build() {

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
    }

    private void runSingleStep() {
        if (runningGameLoop != null) {
            runningGameLoop.singleRun();
        }
        timer.setInterval(new Runnable() {
            @Override
            public void run() {
                runSingleStep();
            }
        }, 20);
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
                sceneLoader.loadFromServer(theSceneId, new DragomeGameResourceLoader(theSceneId));
            }
        });

        Window theWindow = new Window();

        GestureDetector theGestureDetector = new DefaultGestureDetector(theEventManager);

        DragomeGameView theGameView = new DragomeGameView(theRuntime, theCameraComponent, theGestureDetector);

        theGameView.setSize(new Size(theWindow.getClientWidth(), theWindow.getClientHeight()));
        theEventManager.fire(new SetScreenResolution(new Size(theWindow.getClientWidth(), theWindow.getClientHeight())));

        runningGameLoop = gameLoopFactory.create(aGameScene, theGameView, theRuntime);

        theCameraComponent.initializeFor(aGameScene, thePlayerInstance);

        runSingleStep();

        DragomeLogger.info("Scene initialized");
    }
}