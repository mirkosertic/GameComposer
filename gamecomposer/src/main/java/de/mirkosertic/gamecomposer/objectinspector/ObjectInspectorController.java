/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gamecomposer.objectinspector;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.GameSceneCreatedEvent;
import de.mirkosertic.gamecomposer.GameSceneDeletedEvent;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gameengine.arcade.ConstantMovement;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehavior;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehaviorTemplate;
import de.mirkosertic.gameengine.camera.Camera;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplate;
import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectConfigurationChanged;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.core.GameSceneEffectAddedToScene;
import de.mirkosertic.gameengine.core.GameSceneEffectRemovedFromScene;
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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.controlsfx.control.PropertySheet;
import com.sun.javafx.collections.ObservableListWrapper;

@Singleton
public class ObjectInspectorController implements Controller {

    @FXML
    BorderPane content;

    @Inject
    @Any
    Instance<Object> singleObjectFactory;

    private Node view;
    private Object currentSelection;

    private PropertySheet propertySheet;
    private ObservableList<PropertySheet.Item> items;

    private Map<BehaviorType, Class> behaviorTemplateToIdentifier;
    private Map<BehaviorType, Class> behaviorToIdentifier;

    ObjectInspectorController initialize(Node aView) {

        behaviorToIdentifier = new HashMap<>();
        behaviorToIdentifier.put(StaticBehavior.TYPE, Static.class);
        behaviorToIdentifier.put(CameraBehavior.TYPE, Camera.class);
        behaviorToIdentifier.put(PlatformBehavior.TYPE, Platform.class);
        behaviorToIdentifier.put(PhysicsBehavior.TYPE, Physics.class);
        behaviorToIdentifier.put(SpriteBehavior.TYPE, Sprite.class);
        behaviorToIdentifier.put(TextBehavior.TYPE, Text.class);
        behaviorToIdentifier.put(PlayerScoreBehavior.TYPE, PlayerScore.class);
        behaviorToIdentifier.put(ConstantMovementBehavior.TYPE, ConstantMovement.class);
        behaviorTemplateToIdentifier = new HashMap<>();
        behaviorTemplateToIdentifier.put(StaticBehaviorTemplate.TYPE, Static.class);
        behaviorTemplateToIdentifier.put(CameraBehaviorTemplate.TYPE, Camera.class);
        behaviorTemplateToIdentifier.put(PlatformBehaviorTemplate.TYPE, Platform.class);
        behaviorTemplateToIdentifier.put(PhysicsBehaviorTemplate.TYPE, Physics.class);
        behaviorTemplateToIdentifier.put(SpriteBehaviorTemplate.TYPE, Sprite.class);
        behaviorTemplateToIdentifier.put(TextBehaviorTemplate.TYPE, Text.class);
        behaviorTemplateToIdentifier.put(PlayerScoreBehaviorTemplate.TYPE, PlayerScore.class);
        behaviorTemplateToIdentifier.put(ConstantMovementBehaviorTemplate.TYPE, ConstantMovement.class);


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

    public void onGameObjectConfigurationChanged(@Observes GameSceneEffectAddedToScene aEvent) {
        currentSelection = null;
        selectObject(aEvent.instance);
    }

    public void onGameObjectConfigurationChanged(@Observes GameSceneEffectRemovedFromScene aEvent) {
        currentSelection = null;
        selectObject(aEvent.instance.getScene());
    }

    public void onGameObjectConfigurationChanged(@Observes GameObjectConfigurationChanged aEvent) {
        currentSelection = null;
        selectObject(aEvent.object);
    }

    public void onGameSceneDeleted(@Observes GameSceneDeletedEvent aEvent) {
        selectObject(null);
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

                if (aObject instanceof GameScene) {

                    GameScene theScene = (GameScene) aObject;

                    for (GameSceneEffect theEffect : theScene.getPreprocessorEffects()) {
                        Instance<?> theTemplateConfiguratorInstance = singleObjectFactory.select(createQualifier(theEffect.getClass()));
                        if (!theTemplateConfiguratorInstance.isUnsatisfied()) {
                            items.addAll(((ObjectInspectorElementConfigurator) theTemplateConfiguratorInstance.get()).getItemsFor(theEffect));
                        }
                    }

                    for (GameSceneEffect theEffect : theScene.getPostprocessorEffects()) {
                        Instance<?> theTemplateConfiguratorInstance = singleObjectFactory.select(createQualifier(theEffect.getClass()));
                        if (!theTemplateConfiguratorInstance.isUnsatisfied()) {
                            items.addAll(((ObjectInspectorElementConfigurator) theTemplateConfiguratorInstance.get()).getItemsFor(theEffect));
                        }
                    }
                }

                if (aObject instanceof GameObject) {

                    GameObject theGameObject = (GameObject) aObject;

                    for (Map.Entry<BehaviorType,Class> theEntry : behaviorTemplateToIdentifier.entrySet()) {
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

                    for (Map.Entry<BehaviorType, Class> theEntry : behaviorToIdentifier.entrySet()) {
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