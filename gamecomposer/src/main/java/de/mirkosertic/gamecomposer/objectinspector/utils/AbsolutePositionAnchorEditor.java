package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gameengine.type.AbsolutePositionAnchor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class AbsolutePositionAnchorEditor implements PropertyEditor<AbsolutePositionAnchor> {

    private AbsolutePositionAnchor angle;
    private final ComboBox<AbsolutePositionAnchor> editor;
    private PropertySheet.Item item;

    public AbsolutePositionAnchorEditor(PropertySheet.Item aItem) {

        item = aItem;

        editor = new ComboBox<>();
        editor.getItems().addAll(AbsolutePositionAnchor.values());
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
    public AbsolutePositionAnchor getValue() {
        return angle;
    }

    @Override
    public void setValue(AbsolutePositionAnchor aValue) {
        angle = aValue;
        editor.setValue(aValue);
    }
}