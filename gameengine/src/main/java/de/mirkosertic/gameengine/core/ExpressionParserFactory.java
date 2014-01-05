package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.TextExpression;

public interface ExpressionParserFactory {

    ExpressionParser create(TextExpression aTextExpression);
}