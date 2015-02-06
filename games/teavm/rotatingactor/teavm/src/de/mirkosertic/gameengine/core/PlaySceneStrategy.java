package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.SystemException;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.type.Size;

public abstract class PlaySceneStrategy {

    private GameLoop runningGameLoop;

    private final AbstractGameRuntimeFactory runtimeFactory;
    private final GameLoopFactory gameLoopFactory;

    protected PlaySceneStrategy(AbstractGameRuntimeFactory aRuntimeFactory, GameLoopFactory aGameLoopFactory) {
        runtimeFactory = aRuntimeFactory;
        gameLoopFactory = aGameLoopFactory;
    }

    public boolean hasGameLoop() {
        return runningGameLoop != null;
    }

    public GameLoop getRunningGameLoop() {
        return runningGameLoop;
    }

    protected abstract void loadOtherScene(String aSceneId);

    protected abstract Size getScreenSize();

    protected abstract GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector);

    public abstract void handleResize();

    protected void loadingFinished(GameScene aGameScene) {
        runtimeFactory.loadingFinished(aGameScene);
    }

    protected GestureDetector createGestureDetectorFor(GameEventManager aEventManager, CameraBehavior aCamera) {
        return new DefaultGestureDetector(aEventManager, aCamera);
    }

    protected void handleSystemException(SystemException e) {
    }

    public void playScene(GameScene aGameScene) {
        if (runningGameLoop != null) {
            runningGameLoop.shutdown();
        }

        GameRuntime theRuntime = aGameScene.getRuntime();
        GameEventManager theEventManager = theRuntime.getEventManager();

        theEventManager.register(null, SystemException.class, new GameEventListener<SystemException>() {
            @Override
            public void handleGameEvent(SystemException aEvent) {
                handleSystemException(aEvent);
            }
        } );

        loadingFinished(aGameScene);

        GameObject theCameraObject = aGameScene.cameraObjectProperty().get();
        GameObjectInstance theCameraObjectInstance = aGameScene.createFrom(theCameraObject);
        CameraBehavior theCameraBehavior = theCameraObjectInstance.getBehavior(CameraBehavior.class);

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
                loadOtherScene(aEvent.sceneId);
            }
        });

        GestureDetector theGestureDetector = createGestureDetectorFor(theRuntime.getEventManager(), theCameraBehavior);
        GameView theGameView = getOrCreateCurrentGameView(theRuntime, theCameraBehavior, theGestureDetector);

        GameLoop theLoop = gameLoopFactory.create(aGameScene, theGameView, theRuntime);

        Size theScreenResolution = getScreenSize();
        theEventManager.fire(new SetScreenResolution(theScreenResolution));

        runningGameLoop = theLoop;

        theCameraBehavior.initializeFor(aGameScene, thePlayerInstance);
    }
}