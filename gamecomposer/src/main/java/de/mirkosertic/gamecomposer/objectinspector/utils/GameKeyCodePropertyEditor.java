package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gameengine.type.GameKeyCode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class GameKeyCodePropertyEditor implements PropertyEditor<GameKeyCode> {

    private GameKeyCode value;
    private final ComboBox<GameKeyCode> editor;
    private PropertySheet.Item item;

    public GameKeyCodePropertyEditor(PropertySheet.Item aItem) {

        item = aItem;

        editor = new ComboBox<>();
        editor.getItems().addAll(GameKeyCode.values());
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
    public GameKeyCode getValue() {
        return value;
    }

    @Override
    public void setValue(GameKeyCode aValue) {
        value = aValue;
        editor.setValue(aValue);
    }
}