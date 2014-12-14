package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gameengine.type.Size;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class SizePropertyEditor implements PropertyEditor<Size> {

    private Size size;
    private final HBox editor;
    private final TextField width;
    private final TextField height;
    private PropertySheet.Item item;
    private final ValidationSupport validationSupport;

    public SizePropertyEditor(PropertySheet.Item aItem) {

        item = aItem;

        width = new TextField();
        width.setMaxWidth(50);
        width.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (!validationSupport.isInvalid()) {
                        size = size.changeWidth(Integer.parseInt(width.getText()));
                        item.setValue(size);
                    }
                }
            }
        });
        height = new TextField();
        height.setMaxWidth(50);
        height.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (!validationSupport.isInvalid()) {
                        size = size.changeHeight(Integer.parseInt(height.getText()));
                        item.setValue(size);
                    }
                }
            }
        });

        validationSupport = new ValidationSupport();

        Validator theWidthAndHeightValidator = Validator.combine(
                Validator.createEmptyValidator("A value is required"),
                Validator.createPredicateValidator(new IntegerPredicate(), "Value is not a valid number"),
                Validator.createPredicateValidator(new PositiveIntegerPredicate(), "Value must be positive")
        );

        validationSupport.registerValidator(width, theWidthAndHeightValidator);
        validationSupport.registerValidator(height, theWidthAndHeightValidator);

        editor = new HBox();
        editor.setAlignment(Pos.BASELINE_LEFT);
        editor.getChildren().addAll(new Label("W:"), width, new Label("H:"), height);
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public Size getValue() {
        return size;
    }

    @Override
    public void setValue(Size aValue) {
        size = aValue;
        width.setText("" + size.width);
        height.setText("" + size.height);
        validationSupport.redecorate();
    }
}