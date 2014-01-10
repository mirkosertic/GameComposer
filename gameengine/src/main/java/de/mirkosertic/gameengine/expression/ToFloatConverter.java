package de.mirkosertic.gameengine.expression;

class ToFloatConverter implements AutomaticResultConverter<Object, Float> {

    @Override
    public Float convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        if (aValue instanceof Float) {
            return (Float) aValue;
        }
        if (aValue instanceof Number) {
            return ((Number) aValue).floatValue();
        }
        if (aValue instanceof String) {
            return Float.valueOf((String) aValue);
        }
        throw new IllegalArgumentException("Cannot convert " + aValue);
    }
}
