package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gameengine.type.PositionAnchor;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class AbsolutePositionAnchorPropertyEditor implements PropertyEditor<PositionAnchor> {

    private PositionAnchor value;
    private final ComboBox<PositionAnchor> editor;
    private PropertySheet.Item item;

    public AbsolutePositionAnchorPropertyEditor(PropertySheet.Item aItem) {

        item = aItem;

        editor = new ComboBox<>();
        editor.getItems().addAll(PositionAnchor.values());
        editor.setOnAction(event -> item.setValue(editor.getValue()));
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public PositionAnchor getValue() {
        return value;
    }

    @Override
    public void setValue(PositionAnchor aValue) {
        value = aValue;
        editor.setValue(aValue);
    }
}