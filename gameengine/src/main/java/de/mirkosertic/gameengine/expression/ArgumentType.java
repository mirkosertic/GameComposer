package de.mirkosertic.gameengine.expression;

public enum ArgumentType {
    NUMBER {
        public Object convert(Object aValue) {
            if (aValue instanceof Number) {
                return aValue;
            }
            if (aValue instanceof String) {
                String theStrValue = (String) aValue;
                if (theStrValue.contains(".")) {
                    return Double.valueOf(theStrValue);
                } else {
                    return Integer.valueOf(theStrValue);
                }
            }
            throw new IllegalArgumentException("Unknown value : " + aValue);

        }
    },
    STRING {
        public Object convert(Object aObject) {
            if (aObject instanceof String) {
                return aObject;
            }
            if (aObject instanceof Integer) {
                return Integer.toString((Integer) aObject);
            }
            if (aObject instanceof Double) {
                return Double.toString((Double) aObject);
            }
            return aObject.toString();
        }
    };

    public abstract <T> T convert(Object aObject);
}
