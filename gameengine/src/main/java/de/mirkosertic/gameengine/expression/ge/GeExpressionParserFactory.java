package de.mirkosertic.gameengine.expression.ge;

import de.mirkosertic.gameengine.core.ExpressionParser;
import de.mirkosertic.gameengine.core.ExpressionParserFactory;
import de.mirkosertic.gameengine.type.TextExpression;

public class GeExpressionParserFactory implements ExpressionParserFactory {

    @Override
    public ExpressionParser create(TextExpression aTextExpression) {
        return new GeExpressionParser(aTextExpression);
    }
}
