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
package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gamecomposer.objectinspector.PersistentPropertyEditorItem;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.type.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class AnimationPropertyEditor implements PropertyEditor<Animation> {

    private Animation value;

    private final ComboBox<Animation> availableAnimations;

    private final PersistentPropertyEditorItem<AnimationPropertyEditor> item;

    public AnimationPropertyEditor(PropertySheet.Item aItem) {

        item = (PersistentPropertyEditorItem<AnimationPropertyEditor>) aItem;

        availableAnimations = new ComboBox<>();
        availableAnimations.setConverter(new StringConverter<Animation>() {
            @Override
            public String toString(Animation aValue) {
                return aValue.getName();
            }

            @Override
            public Animation fromString(String aVakue) {
                throw new IllegalArgumentException("Not implemented");
            }
        });

        availableAnimations.getItems().addAll(((Sprite)item.getOwner()).getAnimations());
        availableAnimations.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                selectResource();
            }
        });
    }

    @Override
    public Node getEditor() {
        return availableAnimations;
    }

    @Override
    public Animation getValue() {
        return value;
    }

    private void selectResource() {
        Animation theNewAnimation = availableAnimations.getSelectionModel().getSelectedItem();
        item.setValue(theNewAnimation);
        setValue(theNewAnimation);
    }

    @Override
    public void setValue(Animation aValue) {
        value = aValue;
        availableAnimations.getSelectionModel().select(aValue);
    }
}