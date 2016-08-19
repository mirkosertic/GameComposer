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
package de.mirkosertic.gamecomposer.objectinspector.arcaderacer;

import de.mirkosertic.gamecomposer.GameSceneDeletedEvent;
import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.DoublePropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.IntegerPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.PositiveIntegerPropertyEditor;
import de.mirkosertic.gameengine.arcaderacer.ArcadeRacerGameSceneEffect;
import javafx.scene.Node;
import org.controlsfx.control.PropertySheet;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ObjectInspectorElementConfiguratorType(clazz = ArcadeRacerGameSceneEffect.class)
public class ArcadeRacerElementConfigurator implements ObjectInspectorElementConfigurator<ArcadeRacerGameSceneEffect> {

    private static final String CATEGORY_NAME = "90 Effect Arcade Racer";

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    MessageBox messageBox;

    @Inject
    Event<GameSceneDeletedEvent> event;

    @Override
    public List<PropertySheet.Item> getItemsFor(final ArcadeRacerGameSceneEffect aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.speed(), "Speed", "Speed", Optional.of(DoublePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.fieldOfView(), "Field of view", "The field of view", Optional.of(PositiveIntegerPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.viewDepth(), "View depth", "View depth", Optional.of(PositiveIntegerPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.distanceCamera(), "Distance Camera", "Distance camera", Optional.of(IntegerPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.cameraHeight(), "The camera height", "Camera height", Optional.of(DoublePropertyEditor.class)));

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
        theActions.addAction(new ActionPropertyEditorItem.Action("Delete effect...", aEvent -> {
            if (messageBox.showMessageBox((Node) aEvent.getSource(), "Delete effect", "Do you really want to delete this item?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                aObject.getScene().removeEffect(aObject);
            }
        }));
        theResult.add(theActions);
        return theResult;
    }
}