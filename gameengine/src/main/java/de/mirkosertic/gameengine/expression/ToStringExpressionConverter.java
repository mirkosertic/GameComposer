package de.mirkosertic.gameengine.expression;

class ToStringExpressionConverter implements AutomaticResultConverter<Object, String> {

    @Override
    public String convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        return aValue.toString();
    }
}
