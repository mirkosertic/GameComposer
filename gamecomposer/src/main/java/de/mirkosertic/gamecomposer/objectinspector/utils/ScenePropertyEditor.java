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
import de.mirkosertic.gameengine.core.GameScene;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class ScenePropertyEditor implements PropertyEditor<String> {

    private final PersistentPropertyEditorItem<String> item;
    private final ComboBox<GameScene> scenes;

    public ScenePropertyEditor(PropertySheet.Item aItem) {
        item = (PersistentPropertyEditorItem<String>) aItem;
        scenes = new ComboBox<>();
        scenes.setConverter(new StringConverter<GameScene>() {
            @Override
            public String toString(GameScene object) {
                return object.nameProperty().get();
            }

            @Override
            public GameScene fromString(String string) {
                return null;
            }
        });
        for (String theSceneID : item.getPersistenceManager().getScenes()) {
            GameScene theScene = item.getPersistenceManager().getScene(theSceneID);
            scenes.getItems().add(theScene);
        }
    }

    @Override
    public Node getEditor() {
        return scenes;
    }

    @Override
    public String getValue() {
        GameScene theScene = scenes.getSelectionModel().getSelectedItem();
        if (theScene != null) {
            return theScene.nameProperty().get();
        }
        return null;
    }

    @Override
    public void setValue(String aValue) {
        if (aValue == null) {
            scenes.getSelectionModel().clearSelection();
        } else {
            scenes.getSelectionModel().select(item.getPersistenceManager().getScene(aValue));
        }
    }
}