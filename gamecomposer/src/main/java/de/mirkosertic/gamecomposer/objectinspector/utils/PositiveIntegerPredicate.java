package de.mirkosertic.gamecomposer.objectinspector.utils;

import java.util.function.Predicate;

public class PositiveIntegerPredicate implements Predicate<Object> {

    @Override
    public boolean test(Object aObject) {
        if (aObject instanceof String) {
            String theValue = (String) aObject;
            try {
                int theIntValue = Integer.valueOf(theValue);
                return theIntValue > 0;
            } catch (Exception e) {
                return false;
            }
        }
        if (aObject instanceof Integer) {
            return ((int) aObject) > 0;
        }
        return false;
    }
}
