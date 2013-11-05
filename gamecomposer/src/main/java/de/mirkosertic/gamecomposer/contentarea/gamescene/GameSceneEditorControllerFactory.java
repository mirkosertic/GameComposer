package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gamecomposer.FXMLLoaderProducer;
import de.mirkosertic.gamecomposer.GameComposerController;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.javafx.JavaFXGameView;
import de.mirkosertic.gameengine.physics.GamePhysicsManager;
import de.mirkosertic.gameengine.resource.GameResourceCache;
import de.mirkosertic.gameengine.resource.GameResourceLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class GameSceneEditorControllerFactory {

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    Event<ObjectSelectedEvent> objectSelectedEventEvent;

    @Inject
    FXMLLoaderProducer fxmlLoaderProducer;

    public GameSceneEditorController createFor(GameScene aScene) {

        final GameEventManager theEventManager = aScene.getRuntime().getEventManager();
        GameRuntime theRuntime = aScene.getRuntime();
        GameObjectInstanceFactory theInstanceFactory = new GameObjectInstanceFactory(theRuntime);

        // Detect and create a camera
        GameObjectInstance theCameraObject;
        CameraComponent theCameraComponent = null;
        for (GameObject theObject : aScene.getObjects()) {
            CameraComponentTemplate theTemplate = theObject.getComponentTemplate(CameraComponentTemplate.class);
            if (theTemplate != null) {
                theCameraObject = theInstanceFactory.createFrom(theObject);
                theCameraComponent = theCameraObject.getComponent(CameraComponent.class);
            }
        }
        GameResourceLoader theResourceLoader = persistenceManager.createResourceLoaderFor(aScene);
        GameResourceCache theResourceCache = new GameResourceCache(theResourceLoader);

        GamePhysicsManager thePhysicsManager = null;
        for (GameSystem theSystem : theRuntime.getSystems()) {
            if (theSystem instanceof GamePhysicsManager) {
                thePhysicsManager = (GamePhysicsManager) theSystem;
            }
        }

        EditorJXGameView theGameView = new EditorJXGameView(theResourceCache, theCameraComponent, thePhysicsManager);

        GameLoopFactory theGameLoopFactory = new GameLoopFactory();
        GameLoop theMainLoop = theGameLoopFactory.create(aScene, theGameView, theRuntime);

        Thread theMainLoopThread = new Thread(theMainLoop, "MainLoop #" + aScene.getName());

        final CameraComponent theFinalCameraComponent = theCameraComponent;

        // Set defaults, this will be overridden
        theEventManager.fire(new SetScreenResolutionEvent(200, 200));

        try (InputStream fxml = GameSceneEditorController.class.getResourceAsStream("GameSceneEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderProducer.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.gamescene.GameSceneEditor");
            theLoader.setResources(theBundle);
            BorderPane root = (BorderPane) theLoader.load(fxml);

            GameSceneEditorController theController = theLoader.getController();
            theController.centerBorderPane.widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                    theEventManager.fire(new SetScreenResolutionEvent((int) ((double) number2), theFinalCameraComponent.getScreenSize().height));
                }
            });
            theController.centerBorderPane.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                    theEventManager.fire(new SetScreenResolutionEvent(theFinalCameraComponent.getScreenSize().width, (int) ((double) number2)));
                }
            });
            theGameView.widthProperty().bind(theController.centerBorderPane.widthProperty());
            theGameView.heightProperty().bind(theController.centerBorderPane.heightProperty());

            return theController.initialize(aScene, root, theGameView, theMainLoopThread, theCameraComponent, objectSelectedEventEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
