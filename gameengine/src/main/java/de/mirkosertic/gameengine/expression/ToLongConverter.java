package de.mirkosertic.gameengine.expression;

class ToLongConverter implements AutomaticResultConverter<Object, Long> {

    @Override
    public Long convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        if (aValue instanceof Long) {
            return (Long) aValue;
        }
        if (aValue instanceof Number) {
            return ((Number) aValue).longValue();
        }
        if (aValue instanceof String) {
            return Long.valueOf((String) aValue);
        }
        throw new IllegalArgumentException("Cannot convert " + aValue);
    }
}
