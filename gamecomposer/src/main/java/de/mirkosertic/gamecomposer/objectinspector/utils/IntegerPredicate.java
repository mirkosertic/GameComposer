package de.mirkosertic.gamecomposer.objectinspector.utils;

import java.util.function.Predicate;

public class IntegerPredicate implements Predicate<Object> {

    @Override
    public boolean test(Object aObject) {
        if (aObject instanceof String) {
            String theValue = (String) aObject;
            try {
                int theIntValue = Integer.valueOf(theValue);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        if (aObject instanceof Integer) {
            return true;
        }
        return false;
    }
}
