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
package de.mirkosertic.gamecomposer.contentarea.eventsheet.keyeventcondition;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.input.KeyEventCondition;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.GameKeyCodeUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import javax.inject.Inject;

public class KeyEventConditionEditorController implements Controller {

    @Inject
    StringConverterFactory stringConverterFactory;

    @FXML
    ComboBox eventType;

    @FXML
    ComboBox keyCode;

    private GameScene gameScene;
    private KeyEventCondition condition;
    private Node view;

    KeyEventConditionEditorController initialize(Node aView, GameScene aGameScene, KeyEventCondition aCondition) {
        gameScene = aGameScene;
        view = aView;
        condition = aCondition;

        eventType.getItems().clear();
        eventType.getItems().addAll(KeyEventCondition.KeyEventType.values());
        eventType.setConverter(new StringConverter<KeyEventCondition.KeyEventType>() {
            @Override
            public String toString(KeyEventCondition.KeyEventType aValue) {
                return aValue.name();
            }

            @Override
            public KeyEventCondition.KeyEventType fromString(String aValue) {
                // Nonsense here
                return null;
            }
        });
        eventType.getSelectionModel().select(aCondition.eventTypeProperty().get());

        keyCode.getItems().clear();
        keyCode.getItems().addAll(GameKeyCodeUtils.allKeysAsSortedList());
        keyCode.setConverter(stringConverterFactory.createGameKeyCodeStringConverter());
        if (!condition.keyCodeProperty().isNull()) {
            keyCode.getSelectionModel().select(condition.keyCodeProperty().get());
        }
        eventType.setOnAction(actionEvent -> onEventTypeSelected());
        keyCode.setOnAction(actionEvent -> onKeyCodeSelected());

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    private void onEventTypeSelected() {
        condition.eventTypeProperty().set((KeyEventCondition.KeyEventType) eventType.getSelectionModel().getSelectedItem());
    }

    private void onKeyCodeSelected() {
        condition.keyCodeProperty().set((GameKeyCode) keyCode.getSelectionModel().getSelectedItem());
    }
}
