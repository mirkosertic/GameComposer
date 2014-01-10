package de.mirkosertic.gamecomposer.objectinspector;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.GameSceneCreatedEvent;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gameengine.camera.Camera;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplate;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectConfigurationChanged;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.physic.Physics;
import de.mirkosertic.gameengine.physic.PhysicsBehavior;
import de.mirkosertic.gameengine.physic.PhysicsBehaviorTemplate;
import de.mirkosertic.gameengine.physic.Platform;
import de.mirkosertic.gameengine.physic.PlatformBehavior;
import de.mirkosertic.gameengine.physic.PlatformBehaviorTemplate;
import de.mirkosertic.gameengine.physic.Static;
import de.mirkosertic.gameengine.physic.StaticBehavior;
import de.mirkosertic.gameengine.physic.StaticBehaviorTemplate;
import de.mirkosertic.gameengine.playerscore.PlayerScore;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehavior;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorTemplate;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.text.TextBehaviorTemplate;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

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
    Accordion propertyPanels;

    @Inject
    @Any
    Instance<Object> singleObjectFactory;

    private Node view;
    private Object currentSelection;
    private List<ObjectInspectorElementController> currentController;

    ObjectInspectorController initialize(Node aView) {
        currentController = new ArrayList<>();
        propertyPanels.getPanes().clear();
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
        selectObject(aEvent.object);
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
            propertyPanels.getPanes().clear();
            currentController.clear();

            if (aObject instanceof Game) {
                ObjectInspectorElementController theController = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Game.class)).get()).create(aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getPanes().add(thePane);
                currentController.add(theController);
            }
            if (aObject instanceof GameScene) {
                ObjectInspectorElementController theController = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(GameScene.class)).get()).create(aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getPanes().add(thePane);
                currentController.add(theController);
            }
            if (aObject instanceof EventSheet) {
                ObjectInspectorElementController theController = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(EventSheet.class)).get()).create(aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getPanes().add(thePane);
                currentController.add(theController);
            }
            if (aObject instanceof GameObject) {

                GameObject theGameObject = (GameObject) aObject;

                ObjectInspectorElementController theController = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(GameObject.class)).get()).create(aObject);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getPanes().add(thePane);
                currentController.add(theController);

                Static theStaticComponentTemplate = theGameObject.getComponentTemplate(StaticBehaviorTemplate.class);
                if (theStaticComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Static.class)).get()).create(theStaticComponentTemplate);
                    TitledPane theChildPane = new TitledPane("Static", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                Camera theCameraComponentTemplate = theGameObject.getComponentTemplate(CameraBehaviorTemplate.class);
                if (theCameraComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Camera.class)).get()).create(theCameraComponentTemplate);
                    TitledPane theChildPane = new TitledPane("Camera", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                Platform thePlatformComponentTemplate = theGameObject.getComponentTemplate(PlatformBehaviorTemplate.class);
                if (thePlatformComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Platform.class)).get()).create(thePlatformComponentTemplate);
                    TitledPane theChildPane = new TitledPane("Platform", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                Physics thePhysicsComponentTemplate = theGameObject.getComponentTemplate(PhysicsBehaviorTemplate.class);
                if (thePhysicsComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Physics.class)).get()).create(thePhysicsComponentTemplate);
                    TitledPane theChildPane = new TitledPane("Physics", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                Sprite theSpriteComponentTemplate = theGameObject.getComponentTemplate(SpriteBehaviorTemplate.class);
                if (theSpriteComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Sprite.class)).get()).create(theSpriteComponentTemplate);
                    TitledPane theChildPane = new TitledPane("Sprite", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                Text theTextComponentTemplate = theGameObject.getComponentTemplate(TextBehaviorTemplate.class);
                if (theTextComponentTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Text.class)).get()).create(theTextComponentTemplate);
                    TitledPane theChildPane = new TitledPane("Text", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                PlayerScore theScoreTemplate = theGameObject.getComponentTemplate(PlayerScoreBehaviorTemplate.class);
                if (theScoreTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(PlayerScore.class)).get()).create(theScoreTemplate);
                    TitledPane theChildPane = new TitledPane("PlayerScore", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }
            }
            if (aObject instanceof GameObjectInstance) {

                GameObjectInstance theInstance = (GameObjectInstance) aObject;

                ObjectInspectorElementController theController = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(GameObjectInstance.class)).get()).create(theInstance);
                TitledPane thePane = new TitledPane("Properties", theController.getView());
                propertyPanels.getPanes().add(thePane);
                currentController.add(theController);

                Camera theCameraComponent = theInstance.getComponent(CameraBehavior.class);
                if (theCameraComponent != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Camera.class)).get()).create(theCameraComponent);
                    TitledPane theChildPane = new TitledPane("Camera", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                PhysicsBehavior thePhysicsComponent = theInstance.getComponent(PhysicsBehavior.class);
                if (thePhysicsComponent != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Physics.class)).get()).create(thePhysicsComponent);
                    TitledPane theChildPane = new TitledPane("Physics", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                Platform thePlatformComponent = theInstance.getComponent(PlatformBehavior.class);
                if (thePlatformComponent != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Platform.class)).get()).create(thePlatformComponent);
                    TitledPane theChildPane = new TitledPane("Platform", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                Static theStaticComponent = theInstance.getComponent(StaticBehavior.class);
                if (theStaticComponent != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Static.class)).get()).create(theStaticComponent);
                    TitledPane theChildPane = new TitledPane("Static", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                Sprite theSpriteComponent = theInstance.getComponent(SpriteBehavior.class);
                if (theSpriteComponent != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Sprite.class)).get()).create(theSpriteComponent);
                    TitledPane theChildPane = new TitledPane("Sprite", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                Text theTextComponent = theInstance.getComponent(TextBehavior.class);
                if (theTextComponent != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(Text.class)).get()).create(theTextComponent);
                    TitledPane theChildPane = new TitledPane("TextComponent", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }

                PlayerScore theScoreTemplate = theInstance.getComponent(PlayerScoreBehavior.class);
                if (theScoreTemplate != null) {
                    ObjectInspectorElementController theEditor = (ObjectInspectorElementController) ((ObjectInspectorFactory) singleObjectFactory.select(createQualifier(PlayerScore.class)).get()).create(theScoreTemplate);
                    TitledPane theChildPane = new TitledPane("PlayerScoreComponent", theEditor.getView());
                    propertyPanels.getPanes().add(theChildPane);
                    currentController.add(theEditor);
                }
            }
        }
    }
}