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
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.physics.PhysicsComponentTemplate;
import de.mirkosertic.gameengine.physics.PlatformComponentTemplate;
import de.mirkosertic.gameengine.physics.StaticComponentTemplate;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

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

    ObjectInspectorController initialize(Node aView) {
        propertyPanels.getChildren().clear();
        view = aView;
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

    private void selectObject(Object aPbject) {
        propertyPanels.getChildren().clear();
        if (aPbject instanceof Game) {
            ChildController theController = gameEditorControllerFactory.create((Game) aPbject);
            TitledPane thePane = new TitledPane("Properties", theController.getView());
            propertyPanels.getChildren().add(thePane);
        }
        if (aPbject instanceof GameScene) {
            ChildController theController = gameSceneEditorControllerFactory.create((GameScene) aPbject);
            TitledPane thePane = new TitledPane("Properties", theController.getView());
            propertyPanels.getChildren().add(thePane);
        }
        if (aPbject instanceof GameObject) {

            GameObject theGameObject = (GameObject) aPbject;

            ChildController theController = gameObjectEditorControllerFactory.create(theGameObject);
            TitledPane thePane = new TitledPane("Properties", theController.getView());
            propertyPanels.getChildren().add(thePane);

            StaticComponentTemplate theStaticComponentTemplate = theGameObject.getComponentTemplate(StaticComponentTemplate.class);
            if (theStaticComponentTemplate != null) {
                ChildController theEditor = staticTemplateEditorControllerFactory.create(theStaticComponentTemplate);
                TitledPane theChildPane = new TitledPane("StaticComponent", theEditor.getView());
                propertyPanels.getChildren().add(theChildPane);
            }

            CameraComponentTemplate theCameraComponentTemplate = theGameObject.getComponentTemplate(CameraComponentTemplate.class);
            if (theCameraComponentTemplate != null) {
                ChildController theEditor = cameraTemplateEditorControllerFactory.create(theCameraComponentTemplate);
                TitledPane theChildPane = new TitledPane("CameraComponent", theEditor.getView());
                propertyPanels.getChildren().add(theChildPane);
            }

            PlatformComponentTemplate thePlatformComponentTemplate = theGameObject.getComponentTemplate(PlatformComponentTemplate.class);
            if (thePlatformComponentTemplate != null) {
                ChildController theEditor = platformTemplateEditorControllerFactory.create(thePlatformComponentTemplate);
                TitledPane theChildPane = new TitledPane("PlatformComponent", theEditor.getView());
                propertyPanels.getChildren().add(theChildPane);
            }

            PhysicsComponentTemplate thePhysicsComponentTemplate = theGameObject.getComponentTemplate(PhysicsComponentTemplate.class);
            if (thePhysicsComponentTemplate != null) {
                ChildController theEditor = physicsTemplateEditorControllerFactory.create(thePhysicsComponentTemplate);
                TitledPane theChildPane = new TitledPane("PhysicsComponent", theEditor.getView());
                propertyPanels.getChildren().add(theChildPane);
            }

            SpriteComponentTemplate theSpriteComponentTemplate = theGameObject.getComponentTemplate(SpriteComponentTemplate.class);
            if (theSpriteComponentTemplate != null) {
                ChildController theEditor = spriteTemplateEditorControllerFactory.create(theSpriteComponentTemplate);
                TitledPane theChildPane = new TitledPane("SpriteComponent", theEditor.getView());
                propertyPanels.getChildren().add(theChildPane);
            }

        }
        if (aPbject instanceof GameObjectInstance) {
            ChildController theController = gameObjectInstanceEditorControllerFactory.create((GameObjectInstance) aPbject);
            TitledPane thePane = new TitledPane("Properties", theController.getView());
            propertyPanels.getChildren().add(thePane);
        }
    }
}
