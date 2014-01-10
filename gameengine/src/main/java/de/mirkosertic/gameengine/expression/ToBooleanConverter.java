package de.mirkosertic.gameengine.expression;

class ToBooleanConverter implements AutomaticResultConverter<Object, Boolean> {

    @Override
    public Boolean convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        if (aValue instanceof Boolean) {
            return (Boolean) aValue;
        }
        if (aValue instanceof String) {
            return Boolean.valueOf((String) aValue);
        }
        throw new IllegalArgumentException("Cannot convert " + aValue);
    }
}
