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
package de.mirkosertic.gamecomposer.contentarea.eventsheet.instanceleftlayout;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstanceLeftLayoutCondition;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class GameObjectInstanceLeftLayoutEditorController implements Controller {

    @Inject
    StringConverterFactory stringConverterFactory;

    @FXML
    ComboBox objectType;

    private GameScene gameScene;
    private GameObjectInstanceLeftLayoutCondition condition;
    private Node view;

    GameObjectInstanceLeftLayoutEditorController initialize(Node aView, GameScene aGameScene, GameObjectInstanceLeftLayoutCondition aCondition) {
        gameScene = aGameScene;
        view = aView;
        condition = aCondition;

        List<GameObject> theSortedGameObjects = Arrays.asList(aGameScene.getObjects());
        Collections.sort(theSortedGameObjects, (o1, o2) -> o1.nameProperty().get().compareTo(o2.nameProperty().get()));


        objectType.getItems().clear();
        objectType.getItems().addAll(theSortedGameObjects);
        objectType.setConverter(stringConverterFactory.createGameObjectStringConverter());
        objectType.getSelectionModel().select(aCondition.gameObjectProperty().get());

        objectType.setOnAction(actionEvent -> onObjectTypeSelected());

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    private void onObjectTypeSelected() {
        condition.gameObjectProperty().set((GameObject) objectType.getSelectionModel().getSelectedItem());
    }
}
