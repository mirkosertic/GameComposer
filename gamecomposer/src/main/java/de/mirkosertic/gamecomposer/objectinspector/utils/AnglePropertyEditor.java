package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gameengine.type.Angle;
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

public class AnglePropertyEditor implements PropertyEditor<Angle> {

    private Angle angle;
    private final HBox editor;
    private final TextField inputfield;
    private PropertySheet.Item item;
    private final ValidationSupport validationSupport;

    public AnglePropertyEditor(PropertySheet.Item aItem) {

        item = aItem;

        inputfield = new TextField();
        inputfield.setMaxWidth(50);
        inputfield.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (!validationSupport.isInvalid()) {
                        angle = new Angle(Integer.parseInt(inputfield.getText()));
                        item.setValue(angle);
                    }
                }
            }
        });

        validationSupport = new ValidationSupport();

        Validator theWidthAndHeightValidator = Validator.combine(
                Validator.createEmptyValidator("A value is required"),
                Validator.createPredicateValidator(new IntegerPredicate(), "Value is not a valid number")
        );

        validationSupport.registerValidator(inputfield, theWidthAndHeightValidator);

        editor = new HBox();
        editor.setAlignment(Pos.BASELINE_LEFT);
        editor.getChildren().addAll(inputfield, new Label("Degrees"));
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public Angle getValue() {
        return angle;
    }

    @Override
    public void setValue(Angle aValue) {
        angle = aValue;
        inputfield.setText("" + angle.angleInDegrees);
        validationSupport.redecorate();
    }
}