package de.mirkosertic.gameengine.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.camera.FollowCameraProcess;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.physics.PlatformComponent;
import de.mirkosertic.gameengine.processes.StartProcessEvent;
import de.mirkosertic.gameengine.resource.GameResourceCache;
import de.mirkosertic.gameengine.resource.GameResourceLoader;

public class GWTRenderer implements EntryPoint {

    static final String holderId = "canvasholder";
    static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";

    private Canvas canvas;

    @Override
    public void onModuleLoad() {
        canvas = Canvas.createIfSupported();
        if (canvas == null) {
            RootPanel.get(holderId).add(new Label(upgradeMessage));
            return;
        }

        GWTGameRuntimeFactory theRuntimeFactory = new GWTGameRuntimeFactory();
        final GWTGameSceneLoader theSceneLoader = new GWTGameSceneLoader(new GWTGameSceneLoader.GameSceneLoadedListener() {
            @Override
            public void onGameSceneLoaded(GameScene aScene) {
                playScene(aScene);
            }

            @Override
            public void onGameSceneLoadedError(Throwable aThrowable) {
                System.out.println("Error loading scene"+aThrowable);
            }
        }, theRuntimeFactory, new GWTGameResourceLoader("scene1"));

        GWTGameLoader theLoader = new GWTGameLoader(new GWTGameLoader.GameLoadedListener() {
            @Override
            public void onGameLoaded(Game aGame) {
                theSceneLoader.loadFromServer("scene1");
            }

            @Override
            public void onGameLoadedError(Throwable aThrowable) {
                System.out.println("Error loading game"+aThrowable);
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
        GameObjectInstanceFactory theInstanceFactory = new GameObjectInstanceFactory(theRuntime);

        // Detect and create a camera
        GameObjectInstance theCameraObject = null;
        GameObjectInstance thePlayerInstance = null;
        CameraComponent theCameraComponent = null;

        for (GameObject theObject : aGameScene.getObjects()) {
            CameraComponentTemplate theTemplate = theObject.getComponentTemplate(CameraComponentTemplate.class);
            if (theTemplate != null) {
                theCameraObject = theInstanceFactory.createFrom(theObject);
                theCameraComponent = theCameraObject.getComponent(CameraComponent.class);
            }
        }
        for (GameObjectInstance theInstance : aGameScene.getInstances()) {
            PlatformComponent thePlatform = theInstance.getComponent(PlatformComponent.class);
            if (thePlatform != null) {
                thePlayerInstance = theInstance;
            }
        }
        GWTGameView theGameView = new GWTGameView(theRuntime, canvas, theCameraComponent);

        theGameView.setSize(new Size(Window.getClientWidth(), Window.getClientHeight()));
        theEventManager.fire(new SetScreenResolutionEvent(Window.getClientWidth(), Window.getClientHeight()));

        GameLoopFactory theGameLoopFactory = new GameLoopFactory();
        final GameLoop theMainLoop = theGameLoopFactory.create(aGameScene, theGameView, theRuntime);
        // setup timer
        final Timer theTimer = new Timer() {
            @Override
            public void run() {
                theMainLoop.singleRun();
            }
        };
        theTimer.scheduleRepeating(30);

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

        theEventManager.fire(new StartProcessEvent(new FollowCameraProcess(theCameraObject, thePlayerInstance)));
    }

    void resizeCanvas(int aWidth, int aHeight) {
        canvas.setWidth(aWidth + "px");
        canvas.setHeight(aHeight + "px");
        canvas.setCoordinateSpaceWidth(aWidth);
        canvas.setCoordinateSpaceHeight(aHeight);
    }
}