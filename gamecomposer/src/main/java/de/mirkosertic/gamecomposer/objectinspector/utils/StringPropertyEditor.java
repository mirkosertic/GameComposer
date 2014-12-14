package de.mirkosertic.gamecomposer.objectinspector.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class StringPropertyEditor implements PropertyEditor<String> {

    private final TextField text;
    private PropertySheet.Item item;
    private final ValidationSupport validationSupport;

    public StringPropertyEditor(PropertySheet.Item aItem) {

        item = aItem;

        text = new TextField();
        text.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (!validationSupport.isInvalid()) {
                        item.setValue(text.getText());
                    }
                }
            }
        });

        validationSupport = new ValidationSupport();

        Validator theValidator = Validator.combine(
                Validator.createEmptyValidator("A value is required")
        );

        validationSupport.registerValidator(text, theValidator);
    }

    @Override
    public Node getEditor() {
        return text;
    }

    @Override
    public String getValue() {
        return text.getText();
    }

    @Override
    public void setValue(String aValue) {
        text.setText(aValue);
        validationSupport.redecorate();
    }
}