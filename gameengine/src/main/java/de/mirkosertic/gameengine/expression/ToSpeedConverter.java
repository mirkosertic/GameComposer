package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.type.Speed;

class ToSpeedConverter implements AutomaticResultConverter<Object, Speed> {

    @Override
    public Speed convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        if (aValue instanceof Speed) {
            return (Speed) aValue;
        }
        if (aValue instanceof Number) {
            return new Speed(((Number) aValue).intValue());
        }
        if (aValue instanceof String) {
            return new Speed(Integer.valueOf((String) aValue));
        }
        throw new IllegalArgumentException("Cannot convert " + aValue);
    }
}
