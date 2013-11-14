package de.mirkosertic.gameengine.gwt;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.camera.FollowCameraProcess;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.processes.StartProcessEvent;
import de.mirkosertic.gameengine.types.Size;

public class GWTRenderer implements EntryPoint {

    private final static Logger LOGGER = Logger.getLogger(GWTRenderer.class.getName());

    private static final String holderId = "canvasholder";
    private static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";

    private Canvas canvas;

    @Override
    public void onModuleLoad() {
        canvas = Canvas.createIfSupported();
        if (canvas == null) {
            RootPanel.get(holderId).add(new Label(upgradeMessage));
            return;
        }

        final GWTGameRuntimeFactory theRuntimeFactory = new GWTGameRuntimeFactory();

        GWTGameLoader theLoader = new GWTGameLoader(new GWTGameLoader.GameLoadedListener() {
            @Override
            public void onGameLoaded(Game aGame) {
                GWTGameSceneLoader theSceneLoader = new GWTGameSceneLoader(
                        new GWTGameSceneLoader.GameSceneLoadedListener() {
                            @Override
                            public void onGameSceneLoaded(GameScene aScene) {
                                LOGGER.info("Game loaded, loading scene " + aScene.nameProperty().get());

                                // Add the action manager to the running game, now we are ready to go!!
                                ActionManagerFactory theActionManagerFactory = new ActionManagerFactory();
                                ActionManager theActionManager = theActionManagerFactory.create(aScene, aScene.getRuntime().getEventManager());
                                aScene.getRuntime().addSystem(theActionManager);

                                playScene(aScene);
                            }

                            @Override
                            public void onGameSceneLoadedError(Throwable aThrowable) {
                                LOGGER.log(Level.SEVERE, "Error loading game scene", aThrowable);
                            }
                        }, theRuntimeFactory, new GWTGameResourceLoader(aGame.defaultSceneProperty().get()));

                theSceneLoader.loadFromServer(aGame.defaultSceneProperty().get());
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
    }

    private void playScene(GameScene aGameScene) {
        final GameEventManager theEventManager = aGameScene.getRuntime().getEventManager();
        GameRuntime theRuntime = aGameScene.getRuntime();

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
        GWTGameView theGameView = new GWTGameView(theRuntime, canvas, theCameraComponent);

        theGameView.setSize(new Size(Window.getClientWidth(), Window.getClientHeight()));
        theEventManager.fire(new SetScreenResolutionEvent(new Size(Window.getClientWidth(), Window.getClientHeight())));

        GameLoopFactory theGameLoopFactory = new GameLoopFactory();
        final GameLoop theMainLoop = theGameLoopFactory.create(aGameScene, theGameView, theRuntime,
                new DefaultGameLoopThrottle());

        AnimationScheduler.get().requestAnimationFrame(new AnimationScheduler.AnimationCallback() {
            @Override
            public void execute(double v) {
                theMainLoop.singleRun();
                // Request another animation
                AnimationScheduler.get().requestAnimationFrame(this);
            }
        });

        canvas.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent aEvent) {
                GameKeyCode theCode = GWTKeyCodeTranslator.translate(aEvent.getNativeKeyCode());
                if (theCode != null) {
                    theEventManager.fire(new KeyPressedGameEvent(theCode));
                }
            }
        });
        canvas.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent aEvent) {
                GameKeyCode theCode = GWTKeyCodeTranslator.translate(aEvent.getNativeKeyCode());
                if (theCode != null) {
                    theEventManager.fire(new KeyReleasedGameEvent(theCode));
                }
            }
        });
        canvas.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent aKeyPressEvent) {
                GameKeyCode theGameKeyCode = GameKeyCode.fromChar(aKeyPressEvent.getCharCode());
                if (theGameKeyCode != null) {
                    theEventManager.fire(new KeyPressedGameEvent(theGameKeyCode));
                }
            }
        });

        switch (theCameraComponent.getTemplate().typeProperty().get()) {
        case FOLLOWPLAYER:
            theCameraComponent.centerOn(thePlayerInstance);
            theEventManager.fire(new StartProcessEvent(new FollowCameraProcess(theCameraObjectInstance, thePlayerInstance)));
            break;
        case CENTERONSCENE:
            break;
        }

        canvas.setFocus(true);
    }

    void resizeCanvas(int aWidth, int aHeight) {
        canvas.setWidth(aWidth + "px");
        canvas.setHeight(aHeight + "px");
        canvas.setCoordinateSpaceWidth(aWidth);
        canvas.setCoordinateSpaceHeight(aHeight);
    }
}
