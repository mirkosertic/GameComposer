package de.mirkosertic.gameengine.gwt;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import de.mirkosertic.gameengine.action.ActionManager;
import de.mirkosertic.gameengine.action.ActionManagerFactory;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.camera.FollowCameraProcess;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.processes.StartProcess;
import de.mirkosertic.gameengine.types.Size;

public class GWTRenderer implements EntryPoint {

    private final static Logger LOGGER = Logger.getLogger(GWTRenderer.class.getName());

    private static final String holderId = "canvasholder";
    private static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";

    private Canvas canvas;
    private GWTGameRuntimeFactory runtimeFactory;
    private GWTGameSceneLoader sceneLoader;
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

        // This must be done or no events are fired at all
        RootPanel.get().sinkEvents(Event.KEYEVENTS);
    }

    private void handleOnKeyUpEvent(KeyUpEvent aEvent) {
        if (loadedScene != null) {
            GameKeyCode theCode = GWTKeyCodeTranslator.translate(aEvent.getNativeKeyCode());
            if (theCode != null) {
                loadedScene.getRuntime().getEventManager().fire(new KeyReleased(theCode));
            }
        }
    }

    private void handleOnKeyDownEvent(KeyDownEvent aEvent) {
        if (loadedScene != null) {
            GameKeyCode theCode = GWTKeyCodeTranslator.translate(aEvent.getNativeKeyCode());
            if (theCode != null) {
                loadedScene.getRuntime().getEventManager().fire(new KeyPressed(theCode));
            }
        }
    }

    private void handleOnKeyPressedEvent(KeyPressEvent aEvent) {
        if (loadedScene != null) {
            GameKeyCode theCode = GameKeyCode.fromChar(aEvent.getCharCode());
            if (theCode != null) {
                loadedScene.getRuntime().getEventManager().fire(new KeyPressed(theCode));
            }
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

        // Add the action manager to the running game, now we are ready to go!!
        ActionManagerFactory theActionManagerFactory = new ActionManagerFactory();
        ActionManager theActionManager = theActionManagerFactory.create(aGameScene, theEventManager);
        theRuntime.addSystem(theActionManager);

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

        GWTGameView theGameView = new GWTGameView(theRuntime, canvas, theCameraComponent);

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
    }
}