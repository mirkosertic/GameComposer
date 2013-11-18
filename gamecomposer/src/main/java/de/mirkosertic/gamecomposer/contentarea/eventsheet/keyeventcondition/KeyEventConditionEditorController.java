package de.mirkosertic.gamecomposer.contentarea.eventsheet.keyeventcondition;

import de.mirkosertic.gameengine.core.GameKeyCode;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.KeyEventCondition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class KeyEventConditionEditorController {

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
        keyCode.setConverter(new StringConverter<GameKeyCode>() {
            @Override
            public String toString(GameKeyCode aValue) {
                return aValue.name();
            }

            @Override
            public GameKeyCode fromString(String aValue) {
                // Nonsense here
                return null;
            }
        });
        if (!condition.keyCodeProperty().isNull()) {
            keyCode.getSelectionModel().select(condition.keyCodeProperty().get());
        }
        eventType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onEventTypeSelected();
            }
        });
        keyCode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onKeyCodeSelected();
            }
        });

        return this;
    }

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
