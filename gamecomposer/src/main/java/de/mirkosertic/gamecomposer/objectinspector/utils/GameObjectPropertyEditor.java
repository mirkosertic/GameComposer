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

import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class GameObjectPropertyEditor implements PropertyEditor<GameObject> {

    private final PropertyEditorItem<GameObject> item;
    private final ComboBox<GameObject> items;

    public GameObjectPropertyEditor(PropertySheet.Item aItem) {
        item = (PropertyEditorItem<GameObject>) aItem;
        items = new ComboBox<>();
        items.setConverter(new StringConverter<GameObject>() {
            @Override
            public String toString(GameObject object) {
                return object.nameProperty().get();
            }

            @Override
            public GameObject fromString(String string) {
                return null;
            }
        });
        items.setOnAction(event -> item.setValue(getValue()));

        GameScene theScene = (GameScene) item.getOwner();
        for (GameObject theObject : theScene.getObjects()) {
            items.getItems().add(theObject);
        }
    }

    @Override
    public Node getEditor() {
        return items;
    }

    @Override
    public GameObject getValue() {
        return items.getSelectionModel().getSelectedItem();
    }

    @Override
    public void setValue(GameObject aValue) {
        items.getSelectionModel().select(aValue);
    }
}