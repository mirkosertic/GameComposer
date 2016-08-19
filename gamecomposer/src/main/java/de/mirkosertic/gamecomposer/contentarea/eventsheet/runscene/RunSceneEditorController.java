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
package de.mirkosertic.gamecomposer.contentarea.eventsheet.runscene;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionController;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.RunSceneAction;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import javax.inject.Inject;

public class RunSceneEditorController implements ActionController {

    @FXML
    ComboBox scenes;

    @Inject
    PersistenceManager persistenceManager;

    private Node view;
    private RunSceneAction action;

    RunSceneEditorController initialize(Node aView, RunSceneAction aAction) {
        view = aView;
        action = aAction;

        scenes.getItems().clear();
        scenes.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String aSceneId) {
                GameScene theScene = persistenceManager.getScene(aSceneId);
                return theScene.nameProperty().get();
            }

            @Override
            public String fromString(String aValue) {
                // Not required here
                return null;
            }
        });
        scenes.getItems().addAll(persistenceManager.getScenes());
        if (!aAction.gameSceneProperty().isNull()) {
            scenes.getSelectionModel().select(aAction.gameSceneProperty().get());
        }
        scenes.setOnAction(actionEvent -> action.gameSceneProperty().set((String) scenes.getSelectionModel().getSelectedItem()));
        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}