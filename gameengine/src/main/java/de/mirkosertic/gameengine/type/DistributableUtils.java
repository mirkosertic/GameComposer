package de.mirkosertic.gameengine.type;

public final class DistributableUtils {

    public static Object convert(Object aValue) {
        if (aValue instanceof Distributable) {
            return ((Distributable) aValue).serialize();
        }
        return aValue;
    }

    private DistributableUtils() {
    }
}
