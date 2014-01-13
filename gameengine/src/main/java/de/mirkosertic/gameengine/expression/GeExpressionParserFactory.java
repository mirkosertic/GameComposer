package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.core.ExpressionParser;
import de.mirkosertic.gameengine.core.ExpressionParserFactory;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.TextExpression;

public class GeExpressionParserFactory implements ExpressionParserFactory {

    private final Scanner scanner;
    private final Parser parser;
    private final Reflectable functionRegistry;

    public GeExpressionParserFactory() {
        functionRegistry = new BuiltInFunctions();
        scanner = new Scanner();
        parser = new Parser();
    }

    @Override
    public ExpressionParser create(TextExpression aTextExpression) {
        Token[] theInFixTokens = scanner.scan(aTextExpression.expression);
        Token[] theRPNTokens = parser.parse(theInFixTokens);
        return new GeExpressionParser(theRPNTokens, functionRegistry);
    }
}
