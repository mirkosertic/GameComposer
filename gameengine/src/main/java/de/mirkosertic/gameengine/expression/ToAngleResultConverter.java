package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.type.Angle;

class ToAngleResultConverter implements AutomaticResultConverter<Object, Angle> {

    @Override
    public Angle convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        if (aValue instanceof Angle) {
            return (Angle) aValue;
        }
        if (aValue instanceof Number) {
            return new Angle(((Number) aValue).intValue());
        }
        if (aValue instanceof String) {
            return new Angle(Integer.valueOf((String) aValue));
        }
        throw new IllegalArgumentException("Cannot convert " + aValue);
    }
}
