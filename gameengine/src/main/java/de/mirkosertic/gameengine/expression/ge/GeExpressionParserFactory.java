package de.mirkosertic.gameengine.expression.ge;

import de.mirkosertic.gameengine.core.ExpressionParser;
import de.mirkosertic.gameengine.core.ExpressionParserFactory;
import de.mirkosertic.gameengine.type.TextExpression;

public class GeExpressionParserFactory implements ExpressionParserFactory {

    private final Scanner scanner;
    private final Parser parser;
    private final FunctionRegistry functionRegistry;

    public GeExpressionParserFactory() {
        functionRegistry = new DefaultFunctionRegistry();
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
