package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.javafx.JavaFXGameView;
import de.mirkosertic.gameengine.resource.GameResourceCache;
import de.mirkosertic.gameengine.resource.GameResourceLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class GameSceneEditorControllerFactory {

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    Event<ObjectSelectedEvent> objectSelectedEventEvent;

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
        EditorJXGameView theGameView = new EditorJXGameView(theResourceCache, theCameraComponent);

        GameLoopFactory theGameLoopFactory = new GameLoopFactory();
        GameLoop theMainLoop = theGameLoopFactory.create(aScene, theGameView, theRuntime);

        Thread theMainLoopThread = new Thread(theMainLoop, "MainLoop #" + aScene.getName());

        BorderPane theBorderpane = new BorderPane();
        final CameraComponent theFinalCameraComponent = theCameraComponent;
        theBorderpane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                theEventManager.fire(new SetScreenResolutionEvent((int) ((double) number2), theFinalCameraComponent.getScreenSize().getHeight()));
            }
        });
        theBorderpane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                theEventManager.fire(new SetScreenResolutionEvent(theFinalCameraComponent.getScreenSize().getWidth(), (int) ((double) number2)));
            }
        });
        theGameView.widthProperty().bind(theBorderpane.widthProperty());
        theGameView.heightProperty().bind(theBorderpane.heightProperty());

        theBorderpane.setCenter(theGameView);

        // Set defaults, this will be overridden
        theEventManager.fire(new SetScreenResolutionEvent(200, 200));

        final GameSceneEditorController theController = new GameSceneEditorController(aScene, theBorderpane, theGameView, theMainLoopThread, theCameraComponent, objectSelectedEventEvent);
        theGameView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                theController.onMouseClicked(aEvent);
            }
        });

        return theController;
    }
}
