package de.mirkosertic.gameengine.expression;

class ToNumberConverter implements AutomaticResultConverter<Object, Number> {

    @Override
    public Number convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        if (aValue instanceof Number) {
            return (Number) aValue;
        }
        if (aValue instanceof String) {
            String theStrValue = (String) aValue;
            if (theStrValue.contains(".")) {
                return Double.valueOf(theStrValue);
            }
            return Integer.valueOf(theStrValue);
        }
        throw new IllegalArgumentException("Cannot convert " + aValue);
    }
}
