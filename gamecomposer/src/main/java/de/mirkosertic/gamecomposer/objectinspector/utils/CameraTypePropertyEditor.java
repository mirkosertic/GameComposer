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

import de.mirkosertic.gameengine.camera.CameraType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class CameraTypePropertyEditor implements PropertyEditor<CameraType> {

    private CameraType value;
    private final ComboBox<CameraType> editor;
    private PropertySheet.Item item;

    public CameraTypePropertyEditor(PropertySheet.Item aItem) {

        item = aItem;

        editor = new ComboBox<>();
        editor.getItems().addAll(CameraType.values());
        editor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                item.setValue(editor.getValue());
            }
        });
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public CameraType getValue() {
        return value;
    }

    @Override
    public void setValue(CameraType aValue) {
        value = aValue;
        editor.setValue(aValue);
    }
}