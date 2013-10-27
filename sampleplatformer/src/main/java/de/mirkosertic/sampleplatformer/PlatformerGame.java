package de.mirkosertic.sampleplatformer;

import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.camera.CameraComponentFactory;
import de.mirkosertic.gameengine.camera.FollowCameraProcess;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.javafx.JavaFXBitmapDecoder;
import de.mirkosertic.gameengine.javafx.JavaFXGameView;
import de.mirkosertic.gameengine.physics.PhysicsComponentFactory;
import de.mirkosertic.gameengine.physics.PlatformComponentFactory;
import de.mirkosertic.gameengine.physics.StaticComponentFactory;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManager;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.processes.GameProcessManager;
import de.mirkosertic.gameengine.processes.GameProcessManagerFactory;
import de.mirkosertic.gameengine.processes.StartProcessEvent;
import de.mirkosertic.gameengine.resource.ClasspathGameResourceLoader;
import de.mirkosertic.gameengine.resource.GameResourceCache;
import de.mirkosertic.gameengine.resource.GameResourceLoader;
import de.mirkosertic.gameengine.sprites.SpriteComponentFactory;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PlatformerGame extends Application {

    @Override
    public void start(Stage aPrimaryStage) throws IOException {

        Group theRoot = new Group();
        final Scene theScene = new Scene(theRoot, 1024, 600, Color.rgb(0, 0, 0));

        JBox2DGamePhysicsManagerFactory thePhysicsManagerFactory = new JBox2DGamePhysicsManagerFactory();

        // Bootstrap game engine

        // Eventing subsystem
        final GameEventManager theEventManager = new GameEventManager();

        // Resourcemanagement subsystem
        GameResourceLoader theResourceLoader = new ClasspathGameResourceLoader();
        GameResourceCache theResourceCache = new GameResourceCache(theResourceLoader);
        theResourceCache.addResourceDecoder(new JavaFXBitmapDecoder());

        // Processmanagement subsystem
        GameProcessManagerFactory theProcessManagerFactory = new GameProcessManagerFactory();
        GameProcessManager theProcessManager = theProcessManagerFactory.create(theEventManager);

        // Physics system
        JBox2DGamePhysicsManager thePhysicsManager = thePhysicsManagerFactory.create(theEventManager);

        // Runtime
        GameRuntime theGameRuntime = new GameRuntime(theEventManager);
        theGameRuntime.addSystem(theProcessManager);
        theGameRuntime.addSystem(thePhysicsManager);

        GameObjectInstanceFactory theInstanceFactory = new GameObjectInstanceFactory(theGameRuntime);

        GameObject thePlayerObject = new GameObject("Player");
        thePlayerObject.add(new PlatformComponentFactory());
        thePlayerObject.add(new PhysicsComponentFactory());

        GameObject theBrickObject = new GameObject("Brick");
        theBrickObject.add(new StaticComponentFactory());

        GameObject theCameraObject = new GameObject("Camera");
        theCameraObject.add(new CameraComponentFactory());

        GameObjectInstance theCameraInstance = theInstanceFactory.createFrom(theCameraObject);

        SpriteComponentFactory theSpriteComponentFactory = new SpriteComponentFactory();
        theSpriteComponentFactory.setResourceName(new ResourceName("/assets/tiles/wall2_1.png"));
        theBrickObject.add(theSpriteComponentFactory);

        final JavaFXGameView theGameView = new JavaFXGameView(theResourceCache, theCameraInstance.getComponent(CameraComponent.class));
        theGameView.widthProperty().bind(aPrimaryStage.widthProperty());
        theGameView.heightProperty().bind(aPrimaryStage.heightProperty());

        theScene.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                theEventManager.fire(new SetScreenResolutionEvent((int) theScene.getWidth(), (int) theScene.getHeight()));
            }
        });
        theScene.heightProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                theEventManager.fire(new SetScreenResolutionEvent((int) theScene.getWidth(), (int) theScene.getHeight()));
            }
        });

        GameScene theCurrentScene = new GameScene(theGameRuntime);

        // Bricks
        LevelLoader theLevelLoader = new LevelLoader();
        theLevelLoader.loadFrom(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/world.txt"))), theBrickObject, theInstanceFactory, theCurrentScene);

        // Player
        GameObjectInstance thePlayerInstance = theInstanceFactory.createFrom(thePlayerObject);
        thePlayerInstance.setSize(new Size(50, 50));
        theCurrentScene.addGameObjectInstance(thePlayerInstance);

        GameLoopFactory theGameLoopFactory = new GameLoopFactory();
        GameLoop theMainLoop = theGameLoopFactory.create(theCurrentScene, theGameView, theGameRuntime);

        theEventManager.fire(new StartProcessEvent(new FollowCameraProcess(theCameraInstance, thePlayerInstance)));

        final Thread theMainLoopThread = new Thread(theMainLoop, "MainLoop");

        theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                theEventManager.fire(new KeyPressedGameEvent(GameKeyCode.valueOf(keyEvent.getCode().name())));
            }
        });
        theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                theEventManager.fire(new KeyReleasedGameEvent(GameKeyCode.valueOf(keyEvent.getCode().name())));
            }
        });

        theRoot.getChildren().add(theGameView);
        aPrimaryStage.setScene(theScene);

        theEventManager.fire(new SetScreenResolutionEvent((int) theScene.getWidth(), (int) theScene.getHeight()));

        aPrimaryStage.show();
        aPrimaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                theGameView.stopTimer();
                theEventManager.fire(new ShutdownGameEvent());
            }
        });

        theGameView.startTimer();

        theMainLoopThread.start();
    }

    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }
}
