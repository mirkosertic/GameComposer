package de.mirkosertic.gamecomposer.objectinspector.utils;

import java.util.function.Predicate;

public class FloatPredicate implements Predicate<Object> {

    @Override
    public boolean test(Object aObject) {
        if (aObject instanceof String) {
            String theValue = (String) aObject;
            try {
                float theFloatValue = Float.parseFloat(theValue);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        if (aObject instanceof Float) {
            return true;
        }
        return false;
    }
}
