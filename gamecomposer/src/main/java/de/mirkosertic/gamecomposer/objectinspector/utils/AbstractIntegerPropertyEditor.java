package de.mirkosertic.gamecomposer.objectinspector.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public abstract class AbstractIntegerPropertyEditor implements PropertyEditor<Integer> {

    private Integer value;
    private final HBox editor;
    private final TextField inputfield;
    private PropertySheet.Item item;
    private final ValidationSupport validationSupport;

    protected AbstractIntegerPropertyEditor(PropertySheet.Item aItem, Validator aValidator) {

        item = aItem;

        inputfield = new TextField();
        inputfield.setMaxWidth(50);
        inputfield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (!validationSupport.isInvalid()) {
                        value = Integer.valueOf(inputfield.getText());
                        item.setValue(value);
                    }
                }
            }
        });

        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(inputfield, aValidator);

        editor = new HBox();
        editor.setAlignment(Pos.BASELINE_LEFT);
        editor.getChildren().add(inputfield);
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer aValue) {
        value = aValue;
        inputfield.setText("" + value);
        validationSupport.redecorate();
    }
}