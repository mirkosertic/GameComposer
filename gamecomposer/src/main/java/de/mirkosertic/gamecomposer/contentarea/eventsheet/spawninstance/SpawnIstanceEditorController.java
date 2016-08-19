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
package de.mirkosertic.gamecomposer.contentarea.eventsheet.spawninstance;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionController;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.SpawnGameObjectInstanceAction;
import de.mirkosertic.gameengine.type.Position;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class SpawnIstanceEditorController implements ActionController {

    @Inject
    StringConverterFactory stringConverterFactory;

    @FXML
    ComboBox referenceGameObject;

    @FXML
    ComboBox gameObject;

    @FXML
    TextField offsetX;

    @FXML
    TextField offsetY;

    @Inject
    PersistenceManager persistenceManager;

    private Node view;
    private SpawnGameObjectInstanceAction action;

    SpawnIstanceEditorController initialize(GameScene aGameScene, Node aView, SpawnGameObjectInstanceAction aAction) {
        view = aView;
        action = aAction;

        gameObject.getItems().clear();
        gameObject.setConverter(stringConverterFactory.createGameObjectStringConverter());
        referenceGameObject.getItems().clear();
        referenceGameObject.setConverter(stringConverterFactory.createGameObjectStringConverter());

        List<GameObject> theSortedGameObjects = Arrays.asList(aGameScene.getObjects());
        Collections.sort(theSortedGameObjects, (o1, o2) -> o1.nameProperty().get().compareTo(o2.nameProperty().get()));

        gameObject.getItems().addAll(theSortedGameObjects);
        referenceGameObject.getItems().addAll(theSortedGameObjects);
        gameObject.getSelectionModel().select(action.gameObjectProperty().get());
        referenceGameObject.getSelectionModel().select(action.referenceObjectProperty().get());

        PropertyBinder.bindUIToBean(action.offsetProperty(), offsetX.textProperty(), new PropertyBinder.Converter<Position, String>() {
            @Override
            public String beanToUI(Position aValue) {
                return Integer.toString((int) aValue.x);
            }

            @Override
            public Position uiToBean(String aValue) {
                Position theCurrentPosition = action.offsetProperty().get();
                return new Position(Float.parseFloat(aValue), theCurrentPosition.y);
            }
        });
        PropertyBinder.bindUIToBean(action.offsetProperty(), offsetY.textProperty(), new PropertyBinder.Converter<Position, String>() {
            @Override
            public String beanToUI(Position aValue) {
                return Integer.toString((int) aValue.y);
            }

            @Override
            public Position uiToBean(String aValue) {
                Position theCurrentPosition = action.offsetProperty().get();
                return new Position(theCurrentPosition.x, Float.parseFloat(aValue));
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    @FXML
    public void onSetGameObject() {
        action.gameObjectProperty().set((GameObject) gameObject.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void onSetGameObjectReference() {
        action.gameObjectProperty().set((GameObject) referenceGameObject.getSelectionModel().getSelectedItem());
    }
}