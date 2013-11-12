package de.mirkosertic.gamecomposer.objectinspector;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.GameSceneCreatedEvent;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.objectinspector.cameratemplate.CameraTemplateEditorControllerFactory;
import de.mirkosertic.gamecomposer.objectinspector.game.GameEditorControllerFactory;
import de.mirkosertic.gamecomposer.objectinspector.gameobject.GameObjectEditorControllerFactory;
import de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance.GameObjectInstanceEditorControllerFactory;
import de.mirkosertic.gamecomposer.objectinspector.gamescene.GameSceneEditorControllerFactory;
import de.mirkosertic.gamecomposer.objectinspector.physicstemplate.PhysicsTemplateEditorControllerFactory;
import de.mirkosertic.gamecomposer.objectinspector.platformtemplate.PlatformTemplateEditorControllerFactory;
import de.mirkosertic.gamecomposer.objectinspector.spritetemplate.SpriteTemplateEditorControllerFactory;
import de.mirkosertic.gamecomposer.objectinspector.statictemplate.StaticTemplateEditorControllerFactory;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.physics.PhysicsComponentTemplate;
import de.mirkosertic.gameengine.physics.PlatformComponentTemplate;
import de.mirkosertic.gameengine.physics.StaticComponentTemplate;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ObjectInspectorController implements ChildController {

    @FXML
    VBox propertyPanels;

    @Inject
    GameObjectEditorControllerFactory gameObjectEditorControllerFactory;

    @Inject
    GameSceneEditorControllerFactory gameSceneEditorControllerFactory;

    @Inject
    GameEditorControllerFactory gameEditorControllerFactory;

    @Inject
    StaticTemplateEditorControllerFactory staticTemplateEditorControllerFactory;

    @Inject
    CameraTemplateEditorControllerFactory cameraTemplateEditorControllerFactory;

    @Inject
    PlatformTemplateEditorControllerFactory platformTemplateEditorControllerFactory;

    @Inject
    PhysicsTemplateEditorControllerFactory physicsTemplateEditorControllerFactory;

    @Inject
    SpriteTemplateEditorControllerFactory spriteTemplateEditorControllerFactory;

    @Inject
    GameObjectInstanceEditorControllerFactory gameObjectInstanceEditorControllerFactory;

    private Node view;
    private Object currentSelection;
    private List<ObjectInspectorChildController> currentController;

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

    public void onGameObjectConfigurationChanged(@Observes GameObjectConfigurationChangedEvent aEvent) {
        currentSelection = null;
        selectObject(aEvent.objectProperty().get());
    }

    private void selectObject(Object aObject) {
        if (currentSelection != aObject) {
            currentSelection = aObject;

            for (ObjectInspectorChildController theChild : currentController) {
                theChild.cleanup();
            }
            propertyPanels.getChildren().clear();
            currentController.clear();

            if (aObject instanceof Game) {
                ObjectInspectorChildController theController = gameEditorControllerFactory.create((Game) aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getChildren().add(thePane);
                currentController.add(theController);
            }
            if (aObject instanceof GameScene) {
                ObjectInspectorChildController theController = gameSceneEditorControllerFactory.create((GameScene) aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getChildren().add(thePane);
                currentController.add(theController);
            }
            if (aObject instanceof GameObject) {

                GameObject theGameObject = (GameObject) aObject;

                ObjectInspectorChildController theController = gameObjectEditorControllerFactory.create(theGameObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getChildren().add(thePane);
                currentController.add(theController);

                StaticComponentTemplate theStaticComponentTemplate = theGameObject.getComponentTemplate(StaticComponentTemplate.class);
                if (theStaticComponentTemplate != null) {
                    ObjectInspectorChildController theEditor = staticTemplateEditorControllerFactory.create(theStaticComponentTemplate);
                    TitledPane theChildPane = new TitledPane("StaticComponent", theEditor.getView());
                    propertyPanels.getChildren().add(theChildPane);
                    currentController.add(theEditor);
                }

                CameraComponentTemplate theCameraComponentTemplate = theGameObject.getComponentTemplate(CameraComponentTemplate.class);
                if (theCameraComponentTemplate != null) {
                    ObjectInspectorChildController theEditor = cameraTemplateEditorControllerFactory.create(theCameraComponentTemplate);
                    TitledPane theChildPane = new TitledPane("CameraComponent", theEditor.getView());
                    propertyPanels.getChildren().add(theChildPane);
                    currentController.add(theEditor);
                }

                PlatformComponentTemplate thePlatformComponentTemplate = theGameObject.getComponentTemplate(PlatformComponentTemplate.class);
                if (thePlatformComponentTemplate != null) {
                    ObjectInspectorChildController theEditor = platformTemplateEditorControllerFactory.create(thePlatformComponentTemplate);
                    TitledPane theChildPane = new TitledPane("PlatformComponent", theEditor.getView());
                    propertyPanels.getChildren().add(theChildPane);
                    currentController.add(theEditor);
                }

                PhysicsComponentTemplate thePhysicsComponentTemplate = theGameObject.getComponentTemplate(PhysicsComponentTemplate.class);
                if (thePhysicsComponentTemplate != null) {
                    ObjectInspectorChildController theEditor = physicsTemplateEditorControllerFactory.create(thePhysicsComponentTemplate);
                    TitledPane theChildPane = new TitledPane("PhysicsComponent", theEditor.getView());
                    propertyPanels.getChildren().add(theChildPane);
                    currentController.add(theEditor);
                }

                SpriteComponentTemplate theSpriteComponentTemplate = theGameObject.getComponentTemplate(SpriteComponentTemplate.class);
                if (theSpriteComponentTemplate != null) {
                    ObjectInspectorChildController theEditor = spriteTemplateEditorControllerFactory.create(theSpriteComponentTemplate);
                    TitledPane theChildPane = new TitledPane("SpriteComponent", theEditor.getView());
                    propertyPanels.getChildren().add(theChildPane);
                    currentController.add(theEditor);
                }

            }
            if (aObject instanceof GameObjectInstance) {
                ObjectInspectorChildController theController = gameObjectInstanceEditorControllerFactory.create((GameObjectInstance) aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getChildren().add(thePane);
                currentController.add(theController);
            }
        }
    }
}
