package de.mirkosertic.gamecomposer.objectinspector.utils;

import java.util.function.Predicate;

public class DoublePredicate implements Predicate<Object> {

    @Override
    public boolean test(Object aObject) {
        if (aObject instanceof String) {
            String theValue = (String) aObject;
            try {
                double theFloatValue = Double.parseDouble(theValue);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        if (aObject instanceof Double) {
            return true;
        }
        return false;
    }
}
