package de.mirkosertic.gamecomposer.objectinspector;

import com.sun.javafx.collections.ObservableListWrapper;
import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.GameSceneCreatedEvent;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gameengine.arcade.ConstantMovement;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehavior;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehaviorTemplate;
import de.mirkosertic.gameengine.camera.Camera;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplate;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.physic.*;
import de.mirkosertic.gameengine.playerscore.PlayerScore;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehavior;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorTemplate;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.text.TextBehaviorTemplate;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.PropertySheet;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ObjectInspectorController implements Controller {

    public static class Test {

        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @FXML
    BorderPane content;

    @Inject
    @Any
    Instance<Object> singleObjectFactory;

    private Node view;
    private Object currentSelection;

    private PropertySheet propertySheet;
    private ObservableList<PropertySheet.Item> items;

    private Map<Class<? extends BehaviorTemplate>, Class> behaviorTemplateToIdentifier;
    private Map<Class<? extends Behavior>, Class> behaviorToIdentifier;

    ObjectInspectorController initialize(Node aView) {

        behaviorToIdentifier = new HashMap<>();
        behaviorToIdentifier.put(StaticBehavior.class, Static.class);
        behaviorToIdentifier.put(CameraBehavior.class, Camera.class);
        behaviorToIdentifier.put(PlatformBehavior.class, Platform.class);
        behaviorToIdentifier.put(PhysicsBehavior.class, Physics.class);
        behaviorToIdentifier.put(SpriteBehavior.class, Sprite.class);
        behaviorToIdentifier.put(TextBehavior.class, Text.class);
        behaviorToIdentifier.put(PlayerScoreBehavior.class, PlayerScore.class);
        behaviorToIdentifier.put(ConstantMovementBehavior.class, ConstantMovement.class);
        behaviorTemplateToIdentifier = new HashMap<>();
        behaviorTemplateToIdentifier.put(StaticBehaviorTemplate.class, Static.class);
        behaviorTemplateToIdentifier.put(CameraBehaviorTemplate.class, Camera.class);
        behaviorTemplateToIdentifier.put(PlatformBehaviorTemplate.class, Platform.class);
        behaviorTemplateToIdentifier.put(PhysicsBehaviorTemplate.class, Physics.class);
        behaviorTemplateToIdentifier.put(SpriteBehaviorTemplate.class, Sprite.class);
        behaviorTemplateToIdentifier.put(TextBehaviorTemplate.class, Text.class);
        behaviorTemplateToIdentifier.put(PlayerScoreBehaviorTemplate.class, PlayerScore.class);
        behaviorTemplateToIdentifier.put(ConstantMovementBehaviorTemplate.class, ConstantMovement.class);


        items = new ObservableListWrapper<>(new ArrayList<>());
        view = aView;
        propertySheet = new PropertySheet(items);
        propertySheet.setMode(PropertySheet.Mode.CATEGORY);
        propertySheet.setModeSwitcherVisible(false);
        content.setCenter(propertySheet);
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

    private ObjectInspectorElementConfiguratorType createQualifier(final Class aClass) {
        return new ObjectInspectorElementConfiguratorType() {
            @Override
            public Class clazz() {
                return aClass;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ObjectInspectorElementConfiguratorType.class;
            }
        };
    }

    private void selectObject(Object aObject) {
        if (currentSelection != aObject) {
            currentSelection = aObject;

            items.clear();

            if (aObject != null) {

                Instance<?> theConfiguratorInstance = singleObjectFactory.select(createQualifier(aObject.getClass()));
                if (!theConfiguratorInstance.isUnsatisfied()) {
                    items.addAll(((ObjectInspectorElementConfigurator) theConfiguratorInstance.get()).getItemsFor(aObject));
                }
                if (aObject instanceof GameObject) {

                    GameObject theGameObject = (GameObject) aObject;

                    for (Map.Entry<Class<? extends BehaviorTemplate>, Class> theEntry : behaviorTemplateToIdentifier.entrySet()) {
                        BehaviorTemplate theTemplate = theGameObject.getBehaviorTemplate(theEntry.getKey());
                        if (theTemplate != null) {
                            Instance<?> theTemplateConfiguratorInstance = singleObjectFactory.select(createQualifier(theEntry.getValue()));
                            if (!theTemplateConfiguratorInstance.isUnsatisfied()) {
                                items.addAll(((ObjectInspectorElementConfigurator) theTemplateConfiguratorInstance.get()).getItemsFor(theTemplate));
                            }
                        }
                    }
                }
                if (aObject instanceof GameObjectInstance) {

                    GameObjectInstance theInstance = (GameObjectInstance) aObject;

                    for (Map.Entry<Class<? extends Behavior>, Class> theEntry : behaviorToIdentifier.entrySet()) {
                        Behavior theTemplate = theInstance.getBehavior(theEntry.getKey());
                        if (theTemplate != null) {
                            Instance<?> theTemplateConfiguratorInstance = singleObjectFactory.select(createQualifier(theEntry.getValue()));
                            if (!theTemplateConfiguratorInstance.isUnsatisfied()) {
                                items.addAll(((ObjectInspectorElementConfigurator) theTemplateConfiguratorInstance.get()).getItemsFor(theTemplate));
                            }
                        }
                    }
                }
            }
        }
    }
}