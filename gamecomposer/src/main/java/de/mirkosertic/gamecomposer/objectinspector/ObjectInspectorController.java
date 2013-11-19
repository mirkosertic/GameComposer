package de.mirkosertic.gamecomposer.objectinspector;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.GameSceneCreatedEvent;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.physics.PhysicsComponentTemplate;
import de.mirkosertic.gameengine.physics.PlatformComponentTemplate;
import de.mirkosertic.gameengine.physics.StaticComponentTemplate;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ObjectInspectorController implements Controller {

    @FXML
    VBox propertyPanels;

    @Inject
    @Any
    Instance<Object> singleObjectFactory;

    private Node view;
    private Object currentSelection;
    private List<ObjectInspectorElementController> currentController;

    ObjectInspectorController initialize(Node aView) {
        currentController = new ArrayList<>();
        propertyPanels.getChildren().clear();
        view = aView;
        currentSelection = null;
        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    public void onGameSceneCreated(@Observes GameSceneCreatedEvent aEvent) {
        selectObject(aEvent.getGameScene());
    }

    public void onObjectSelected(@Observes ObjectSelectedEvent aEvent) {
        selectObject(aEvent.getSelectedObject());
    }

    public void onGameObjectConfigurationChanged(@Observes GameObjectConfigurationChanged aEvent) {
        currentSelection = null;
        selectObject(aEvent.objectProperty().get());
    }

    private ObjectInspectorFactoryType createQualifier(final Class aClass) {
        return new ObjectInspectorFactoryType() {
            @Override
            public Class clazz() {
                return aClass;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ObjectInspectorFactoryType.class;
            }
        };
    }

    private void selectObject(Object aObject) {
        if (currentSelection != aObject) {
            currentSelection = aObject;

            for (ObjectInspectorElementController theChild : currentController) {
                theChild.cleanup();
            }
            propertyPanels.getChildren().clear();
            currentController.clear();

            if (aObject instanceof Game) {
                ObjectInspectorElementController theController = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Game.class)).get()).create(aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getChildren().add(thePane);
                currentController.add(theController);
            }
            if (aObject instanceof GameScene) {
                ObjectInspectorElementController theController = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(GameScene.class)).get()).create(aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getChildren().add(thePane);
                currentController.add(theController);
            }
            if (aObject instanceof EventSheet) {
                ObjectInspectorElementController theController = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(EventSheet.class)).get()).create(aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getChildren().add(thePane);
                currentController.add(theController);
            }
            if (aObject instanceof GameObject) {

                GameObject theGameObject = (GameObject) aObject;

                ObjectInspectorElementController theController = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(GameObject.class)).get()).create(aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getChildren().add(thePane);
                currentController.add(theController);

                StaticComponentTemplate theStaticComponentTemplate = theGameObject.getComponentTemplate(StaticComponentTemplate.class);
                if (theStaticComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(StaticComponentTemplate.class)).get()).create(theStaticComponentTemplate);
                    TitledPane theChildPane = new TitledPane("StaticComponent", theEditor.getView());
                    propertyPanels.getChildren().add(theChildPane);
                    currentController.add(theEditor);
                }

                CameraComponentTemplate theCameraComponentTemplate = theGameObject.getComponentTemplate(CameraComponentTemplate.class);
                if (theCameraComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(CameraComponentTemplate.class)).get()).create(theCameraComponentTemplate);
                    TitledPane theChildPane = new TitledPane("CameraComponent", theEditor.getView());
                    propertyPanels.getChildren().add(theChildPane);
                    currentController.add(theEditor);
                }

                PlatformComponentTemplate thePlatformComponentTemplate = theGameObject.getComponentTemplate(PlatformComponentTemplate.class);
                if (thePlatformComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(PlatformComponentTemplate.class)).get()).create(thePlatformComponentTemplate);
                    TitledPane theChildPane = new TitledPane("PlatformComponent", theEditor.getView());
                    propertyPanels.getChildren().add(theChildPane);
                    currentController.add(theEditor);
                }

                PhysicsComponentTemplate thePhysicsComponentTemplate = theGameObject.getComponentTemplate(PhysicsComponentTemplate.class);
                if (thePhysicsComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(PhysicsComponentTemplate.class)).get()).create(thePhysicsComponentTemplate);
                    TitledPane theChildPane = new TitledPane("PhysicsComponent", theEditor.getView());
                    propertyPanels.getChildren().add(theChildPane);
                    currentController.add(theEditor);
                }

                SpriteComponentTemplate theSpriteComponentTemplate = theGameObject.getComponentTemplate(SpriteComponentTemplate.class);
                if (theSpriteComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(SpriteComponentTemplate.class)).get()).create(theSpriteComponentTemplate);
                    TitledPane theChildPane = new TitledPane("SpriteComponent", theEditor.getView());
                    propertyPanels.getChildren().add(theChildPane);
                    currentController.add(theEditor);
                }

            }
            if (aObject instanceof GameObjectInstance) {
                ObjectInspectorElementController theController = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(GameObjectInstance.class)).get()).create(aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getChildren().add(thePane);
                currentController.add(theController);
            }
        }
    }
}
