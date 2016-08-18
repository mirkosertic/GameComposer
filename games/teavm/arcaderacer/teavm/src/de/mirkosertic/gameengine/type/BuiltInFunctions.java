package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

public abstract class BuiltInFunctions implements Reflectable {

    @ReflectiveMethod
    public Number systime() {
        return System.currentTimeMillis();
    }

    @ReflectiveMethod
    public abstract String formatTime(Number aTimeInMilis, String aPattern);

    @ReflectiveMethod
    public Number max(Number aNumber1, Number aNumber2) {
        if (aNumber1 instanceof Integer) {
            return Math.max(aNumber1.intValue(), aNumber2.intValue());
        }
        return Math.max(aNumber1.doubleValue(), aNumber2.doubleValue());
    }

    @ReflectiveMethod
    public Number min(Number aNumber1, Number aNumber2) {
        if (aNumber1 instanceof Integer) {
            return Math.min(aNumber1.intValue(), aNumber2.intValue());
        }
        return Math.min(aNumber1.doubleValue(), aNumber2.doubleValue());
    }

    @ReflectiveMethod
    public Object nvl(Object aObject, Object aDefault) {
        if (aObject == null) {
            return aDefault;
        }
        return aObject;
    }
}