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
package de.mirkosertic.gamecomposer.objectinspector.starfield;

import de.mirkosertic.gamecomposer.GameSceneDeletedEvent;
import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.ColorPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.FloatPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.PositiveIntegerPropertyEditor;
import de.mirkosertic.gameengine.starfield.StarfieldGameSceneEffect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.controlsfx.control.PropertySheet;

@ObjectInspectorElementConfiguratorType(clazz = StarfieldGameSceneEffect.class)
public class StarfieldElementConfigurator implements ObjectInspectorElementConfigurator<StarfieldGameSceneEffect> {

    private static final String CATEGORY_NAME = "80 Effect Starfield";

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    MessageBox messageBox;

    @Inject
    Event<GameSceneDeletedEvent> event;

    @Override
    public List<PropertySheet.Item> getItemsFor(final StarfieldGameSceneEffect aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.numberofStars(), "Number of stars", "The number of stars", Optional.of(PositiveIntegerPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.starSpeed(), "Star speed", "The speed of the stars", Optional.of(FloatPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.color(), "Star color", "The color of the stars", Optional.of(ColorPropertyEditor.class)));

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