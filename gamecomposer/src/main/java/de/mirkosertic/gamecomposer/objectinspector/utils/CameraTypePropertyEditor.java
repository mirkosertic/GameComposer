package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gameengine.camera.CameraType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class CameraTypePropertyEditor implements PropertyEditor<CameraType> {

    private CameraType value;
    private final ComboBox<CameraType> editor;
    private PropertySheet.Item item;

    public CameraTypePropertyEditor(PropertySheet.Item aItem) {

        item = aItem;

        editor = new ComboBox<>();
        editor.getItems().addAll(CameraType.values());
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
    public CameraType getValue() {
        return value;
    }

    @Override
    public void setValue(CameraType aValue) {
        value = aValue;
        editor.setValue(aValue);
    }
}