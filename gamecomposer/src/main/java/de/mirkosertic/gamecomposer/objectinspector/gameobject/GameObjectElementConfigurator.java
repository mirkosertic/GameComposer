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
package de.mirkosertic.gamecomposer.objectinspector.gameobject;

import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.SizePropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehaviorTemplate;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.physic.PhysicsBehaviorTemplate;
import de.mirkosertic.gameengine.physic.PlatformBehaviorTemplate;
import de.mirkosertic.gameengine.physic.StaticBehaviorTemplate;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorTemplate;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.text.TextBehaviorTemplate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.controlsfx.control.PropertySheet;

@ObjectInspectorElementConfiguratorType(clazz = GameObject.class)
public class GameObjectElementConfigurator implements ObjectInspectorElementConfigurator<GameObject> {

    private static final String CATEGORY_NAME = "03 GameObject";

    @Inject
    MessageBox messageBox;

    @Override
    public List<PropertySheet.Item> getItemsFor(final GameObject aObject) {
        ArrayList<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the object", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.sizeProperty(), "Size", "The size of the object", Optional.of(SizePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.visibleProperty(), "Visible", "The visibility of the object"));

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
        theActions.addAction(new ActionPropertyEditorItem.Action("Delete object...", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                if (messageBox.showMessageBox((Node) aEvent.getSource(), "Delete object", "Do you really want to delete this item?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                    aObject.getGameScene().removeGameObject(aObject);
                }
            }
        }));
        theResult.add(theActions);

        List<ActionPropertyEditorItem.Action> theAddBehaviorActions = new ArrayList<>();
        if (aObject.getBehaviorTemplate(CameraBehaviorTemplate.TYPE) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add camera behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new CameraBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(PhysicsBehaviorTemplate.TYPE) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add physics behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new PhysicsBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(PlatformBehaviorTemplate.TYPE) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add platform behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new PlatformBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(PlayerScoreBehaviorTemplate.TYPE) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add player score behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new PlayerScoreBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(SpriteBehaviorTemplate.TYPE) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add sprite behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new SpriteBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(StaticBehaviorTemplate.TYPE) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add static behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new StaticBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(TextBehaviorTemplate.TYPE) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add text behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new TextBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }
        if (aObject.getBehaviorTemplate(ConstantMovementBehaviorTemplate.TYPE) == null) {
            theAddBehaviorActions.add(new ActionPropertyEditorItem.Action("Add constant movement behavior", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    aObject.getGameScene().addBehaviorToObject(
                            aObject, new ConstantMovementBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(), aObject));
                }
            }));
        }

        if (!theAddBehaviorActions.isEmpty()) {
            ActionPropertyEditorItem theAddActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
            for (ActionPropertyEditorItem.Action theSingleAction : theAddBehaviorActions) {
                theAddActions.addAction(theSingleAction);
            }
            theResult.add(theAddActions);
        }

        return theResult;
    }
}