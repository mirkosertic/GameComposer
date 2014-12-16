package de.mirkosertic.gamecomposer.objectinspector.utils;

import java.util.function.Predicate;

public class LongPredicate implements Predicate<Object> {

    @Override
    public boolean test(Object aObject) {
        if (aObject instanceof String) {
            String theValue = (String) aObject;
            try {
                long theIntValue = Long.parseLong(theValue);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        if (aObject instanceof Long) {
            return true;
        }
        return false;
    }
}
