
package de.mirkosertic.sampleplatformer;

import de.mirkosertic.gameengine.camera.*;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.javafx.JavaFXClasspathGameResourceLoader;
import de.mirkosertic.gameengine.javafx.JavaFXGameView;
import de.mirkosertic.gameengine.javafx.ThreadGameLoopThrottle;
import de.mirkosertic.gameengine.physics.*;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManager;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.processes.GameProcessManager;
import de.mirkosertic.gameengine.processes.GameProcessManagerFactory;
import de.mirkosertic.gameengine.processes.StartProcessEvent;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.sprites.SpriteComponentUnmarshaller;
import de.mirkosertic.gameengine.types.ResourceName;
import de.mirkosertic.gameengine.types.Size;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class PlatformerGame extends Application {

    @Override
    public void start(Stage aPrimaryStage) throws IOException {

        Group theRoot = new Group();
        final Scene theScene = new Scene(theRoot, 1024, 600, Color.rgb(0, 0, 0));

        JBox2DGamePhysicsManagerFactory thePhysicsManagerFactory = new JBox2DGamePhysicsManagerFactory();

        // Bootstrap game engine

        // Eventing subsystem
        final GameEventManager theEventManager = new GameEventManager();

        // Processmanagement subsystem
        GameProcessManagerFactory theProcessManagerFactory = new GameProcessManagerFactory();
        GameProcessManager theProcessManager = theProcessManagerFactory.create(theEventManager);

        // Physics system
        JBox2DGamePhysicsManager thePhysicsManager = thePhysicsManagerFactory.create(theEventManager);

        // Runtime
        GameRuntime theGameRuntime = new GameRuntime(theEventManager, new JavaFXClasspathGameResourceLoader());
        theGameRuntime.addSystem(theProcessManager);
        theGameRuntime.addSystem(thePhysicsManager);

        IORegistry theRegistry = theGameRuntime.getIORegistry();
        theRegistry.registerTemplateUnmarshaller(new PhysicsComponentTemplateUnmarshaller());
        theRegistry.registerTemplateUnmarshaller(new CameraComponentTemplateUnmarshaller());
        theRegistry.registerTemplateUnmarshaller(new SpriteComponentTemplateUnmarshaller());
        theRegistry.registerTemplateUnmarshaller(new StaticComponentTemplateUnmarshaller());
        theRegistry.registerTemplateUnmarshaller(new PlatformComponentTemplateUnmarshaller());

        theRegistry.registerComponentUnmarshaller(new PhysicsComponentUnmarshaller());
        theRegistry.registerComponentUnmarshaller(new CameraComponentUnmarshaller());
        theRegistry.registerComponentUnmarshaller(new SpriteComponentUnmarshaller());
        theRegistry.registerComponentUnmarshaller(new StaticComponentUnmarshaller());
        theRegistry.registerComponentUnmarshaller(new PlatformComponentUnmarshaller());

        GameObjectInstanceFactory theInstanceFactory = new GameObjectInstanceFactory(theGameRuntime);

        Game theGame = new Game();
        theGame.nameProperty().set("Examplegame");
        theGame.addScene("scene1");

        GameScene theCurrentScene = new GameScene(theGameRuntime);
        theCurrentScene.nameProperty().set("Testscene");

        GameObject thePlayerObject = new GameObject(theCurrentScene, "Player");
        thePlayerObject.add(new PlatformComponentTemplate(thePlayerObject));
        thePlayerObject.add(new PhysicsComponentTemplate(thePlayerObject));
        theCurrentScene.addGameObject(thePlayerObject);

        GameObject theBrickObject = new GameObject(theCurrentScene, "Brick");
        theBrickObject.add(new StaticComponentTemplate(theBrickObject));
        theCurrentScene.addGameObject(theBrickObject);

        GameObject theCameraObject = new GameObject(theCurrentScene, "Camera");
        theCameraObject.add(new CameraComponentTemplate(theCameraObject));
        theCurrentScene.addGameObject(theCameraObject);

        GameObjectInstance theCameraInstance = theInstanceFactory.createFrom(theCameraObject);

        SpriteComponentTemplate theSpriteComponentFactory = new SpriteComponentTemplate(theBrickObject);
        theSpriteComponentFactory.resourceNameProperty().set(new ResourceName("/assets/tiles/wall2_1.png"));
        theBrickObject.add(theSpriteComponentFactory);

        final JavaFXGameView theGameView = new JavaFXGameView(theGameRuntime, theCameraInstance.getComponent(CameraComponent.class));
        theGameView.widthProperty().bind(aPrimaryStage.widthProperty());
        theGameView.heightProperty().bind(aPrimaryStage.heightProperty());

        theScene.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                theEventManager.fire(new SetScreenResolutionEvent(new Size((int) theScene.getWidth(), (int) theScene.getHeight())));
            }
        });
        theScene.heightProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                theEventManager.fire(new SetScreenResolutionEvent(new Size((int) theScene.getWidth(), (int) theScene.getHeight())));
            }
        });

        // Bricks
        LevelLoader theLevelLoader = new LevelLoader();
        theLevelLoader.loadFrom(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/world.txt"))), theBrickObject, theInstanceFactory, theCurrentScene);

        // Player
        GameObjectInstance thePlayerInstance = theInstanceFactory.createFrom(thePlayerObject);
        thePlayerInstance.getOwnerGameObject().sizeProperty().setQuietly(new Size(50, 50));
        thePlayerInstance.nameProperty().set("Player#1");
        theCurrentScene.addGameObjectInstance(thePlayerInstance);

        Map<String, Object> theSerializedResult = theCurrentScene.serialize();

        ObjectMapper theObjectMapper = new ObjectMapper();

        ObjectWriter theObjWriter = theObjectMapper.writerWithDefaultPrettyPrinter();
        theObjWriter.writeValue(new File("C:\\source\\idea_projects\\GameEngine\\sampleplatformer\\data\\scene1\\scene.json"), theSerializedResult);
        theObjWriter.writeValue(new File("C:\\source\\idea_projects\\GameEngine\\sampleplatformer\\data\\game.json"), theGame.serialize());

        GameLoopFactory theGameLoopFactory = new GameLoopFactory();
        GameLoop theMainLoop = theGameLoopFactory.create(theCurrentScene, theGameView, theGameRuntime,new ThreadGameLoopThrottle());

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

        theEventManager.fire(new SetScreenResolutionEvent(new Size((int) theScene.getWidth(), (int) theScene.getHeight())));

        aPrimaryStage.show();
        aPrimaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                theGameView.stopTimer();
                theEventManager.fire(new GameShutdownEvent());
            }
        });

        theGameView.startTimer();

        theMainLoopThread.start();
    }

    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }
}
