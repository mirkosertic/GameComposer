package de.mirkosertic.gamecomposer.objectinspector.utils;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.validation.Validator;

public class PositiveIntegerPropertyEditor extends AbstractIntegerPropertyEditor {

    public PositiveIntegerPropertyEditor(PropertySheet.Item aItem) {
        super(aItem, Validator.combine(
                Validator.createEmptyValidator("A value is required"),
                Validator.createPredicateValidator(new PositiveIntegerPredicate(), "Value is not a valid number")
        ));
    }
}