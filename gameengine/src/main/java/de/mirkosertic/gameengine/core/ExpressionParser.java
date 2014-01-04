package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.TextExpression;

public class ExpressionParser {

    private final TextExpression textExpression;

    ExpressionParser(TextExpression aExpression) {
        textExpression = aExpression;
    }

    public String evaluate() {
        return textExpression.expression;
    }
}
