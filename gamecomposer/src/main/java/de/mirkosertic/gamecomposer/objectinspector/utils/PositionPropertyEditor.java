package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gameengine.type.Position;
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

public class PositionPropertyEditor implements PropertyEditor<Position> {

    private Position position;
    private final HBox editor;
    private final TextField x;
    private final TextField y;
    private PropertySheet.Item item;
    private final ValidationSupport validationSupport;

    public PositionPropertyEditor(PropertySheet.Item aItem) {

        item = aItem;

        x = new TextField();
        x.setMaxWidth(50);
        x.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (!validationSupport.isInvalid()) {
                        position = position.changeX((float) Integer.parseInt(x.getText()));
                        item.setValue(position);
                    }
                }
            }
        });
        y = new TextField();
        y.setMaxWidth(50);
        y.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (!validationSupport.isInvalid()) {
                        position = position.changeY((float) Integer.parseInt(y.getText()));
                        item.setValue(position);
                    }
                }
            }
        });

        validationSupport = new ValidationSupport();

        Validator theValidator = Validator.combine(
                Validator.createEmptyValidator("A value is required"),
                Validator.createPredicateValidator(new IntegerPredicate(), "Value is not a valid number"),
                Validator.createPredicateValidator(new PositiveIntegerPredicate(), "Value must be positive")
        );

        validationSupport.registerValidator(x, theValidator);
        validationSupport.registerValidator(y, theValidator);

        editor = new HBox();
        editor.setAlignment(Pos.BASELINE_LEFT);
        editor.getChildren().addAll(new Label("X:"), x, new Label("Y:"), y);
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public Position getValue() {
        return position;
    }

    @Override
    public void setValue(Position aValue) {
        position = aValue;
        x.setText("" + (int)position.x);
        y.setText("" + (int)position.y);
        validationSupport.redecorate();
    }
}