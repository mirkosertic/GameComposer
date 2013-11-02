package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.javafx.JavaFXGameView;
import de.mirkosertic.gameengine.resource.GameResourceCache;
import de.mirkosertic.gameengine.resource.GameResourceLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;

public class GameSceneEditorControllerFactory {

    @Inject
    PersistenceManager persistenceManager;

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
        JavaFXGameView theGameView = new JavaFXGameView(theResourceCache, theCameraComponent);

        GameLoopFactory theGameLoopFactory = new GameLoopFactory();
        GameLoop theMainLoop = theGameLoopFactory.create(aScene, theGameView, theRuntime);

        Thread theMainLoopThread = new Thread(theMainLoop, "MainLoop #" + aScene.getName());

        BorderPane theBorderpane = new BorderPane();
        final CameraComponent theFinalCameraComponent = theCameraComponent;
        theBorderpane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                theEventManager.fire(new SetScreenResolutionEvent((int) ((double) number2), (int) theFinalCameraComponent.getScreenSize().getHeight()));
            }
        });
        theBorderpane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                theEventManager.fire(new SetScreenResolutionEvent((int) theFinalCameraComponent.getScreenSize().getWidth(), (int) ((double) number2)));
            }
        });
        theGameView.widthProperty().bind(theBorderpane.widthProperty());
        theGameView.heightProperty().bind(theBorderpane.heightProperty());

        theBorderpane.setCenter(theGameView);

        // Set defaults, this will be overridden
        theEventManager.fire(new SetScreenResolutionEvent((int) 200, (int) 200));

        return new GameSceneEditorController(aScene, theBorderpane, theGameView, theMainLoopThread);
    }
}
