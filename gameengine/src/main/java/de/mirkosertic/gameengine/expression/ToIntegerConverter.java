package de.mirkosertic.gameengine.expression;

class ToIntegerConverter implements AutomaticResultConverter<Object, Integer> {

    @Override
    public Integer convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        if (aValue instanceof Integer) {
            return (Integer) aValue;
        }
        if (aValue instanceof Number) {
            return ((Number) aValue).intValue();
        }
        if (aValue instanceof String) {
            return Integer.valueOf((String) aValue);
        }
        throw new IllegalArgumentException("Cannot convert " + aValue);
    }
}
