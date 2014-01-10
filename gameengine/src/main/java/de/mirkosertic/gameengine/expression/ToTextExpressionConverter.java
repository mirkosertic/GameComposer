package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.type.TextExpression;

class ToTextExpressionConverter implements AutomaticResultConverter<Object, TextExpression> {

    @Override
    public TextExpression convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        if (aValue instanceof TextExpression) {
            return (TextExpression) aValue;
        }
        if (aValue instanceof String) {
            return new TextExpression((String) aValue);
        }
        throw new IllegalArgumentException("Cannot convert " + aValue);
    }
}
