package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.TextExpression;

public class ExpressionParserFactory {

    public ExpressionParser create(TextExpression aTextExpression) {
        return new ExpressionParser(aTextExpression);
    }
}
