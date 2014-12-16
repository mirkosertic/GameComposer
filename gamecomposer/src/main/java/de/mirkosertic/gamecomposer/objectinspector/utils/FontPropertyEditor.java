package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gameengine.type.Font;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class FontPropertyEditor implements PropertyEditor<Font> {

    private Font value;
    private final HBox editor;
    private final ComboBox<Font.FontName> fontNames;
    private final TextField width;
    private PropertySheet.Item item;
    private final ValidationSupport validationSupport;

    public FontPropertyEditor(PropertySheet.Item aItem) {

        item = aItem;

        width = new TextField();
        width.setMaxWidth(50);
        width.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    updateFont();
                }
            }
        });
        fontNames = new ComboBox<>();
        fontNames.getItems().addAll(Font.FontName.values());
        fontNames.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                updateFont();
            }
        });

        validationSupport = new ValidationSupport();

        Validator theValidator = Validator.combine(
                Validator.createEmptyValidator("A value is required"),
                Validator.createPredicateValidator(new IntegerPredicate(), "Value is not a valid number"),
                Validator.createPredicateValidator(new PositiveIntegerPredicate(), "Value must be positive")
        );

        validationSupport.registerValidator(width, theValidator);

        editor = new HBox();
        editor.setAlignment(Pos.BASELINE_LEFT);
        editor.getChildren().addAll(fontNames, new Label("Size:"), width);
    }

    private void updateFont() {
        if (!validationSupport.isInvalid() && !fontNames.getSelectionModel().isEmpty() && !StringUtils.isEmpty(width.getText())) {
            value = new Font(fontNames.getSelectionModel().getSelectedItem(), Integer.valueOf(width.getText()));
            item.setValue(value);
        }
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public Font getValue() {
        return value;
    }

    @Override
    public void setValue(Font aValue) {
        fontNames.getSelectionModel().select(aValue.name);
        width.setText("" + aValue.size);
        validationSupport.redecorate();
        value = aValue;
    }
}