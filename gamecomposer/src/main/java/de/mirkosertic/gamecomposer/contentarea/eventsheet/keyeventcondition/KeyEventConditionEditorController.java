package de.mirkosertic.gamecomposer.contentarea.eventsheet.keyeventcondition;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.input.KeyEventCondition;
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
        keyCode.getItems().addAll(GameKeyCode.allKeysAsSortedList());
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
