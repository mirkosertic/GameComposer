package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gameengine.type.TextExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class TextExpressionPropertyEditor implements PropertyEditor<TextExpression> {

    private final TextField text;
    private PropertySheet.Item item;
    private final ValidationSupport validationSupport;

    public TextExpressionPropertyEditor(PropertySheet.Item aItem) {

        item = aItem;

        text = new TextField();
        text.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (!validationSupport.isInvalid()) {
                        item.setValue(new TextExpression(text.getText()));
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
    public TextExpression getValue() {
        return new TextExpression(text.getText());
    }

    @Override
    public void setValue(TextExpression aValue) {
        text.setText(aValue.expression);
        validationSupport.redecorate();
    }
}