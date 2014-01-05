package de.mirkosertic.gameengine.expression.ge;

import de.mirkosertic.gameengine.core.ExpressionParser;
import de.mirkosertic.gameengine.event.PropertyAware;
import de.mirkosertic.gameengine.type.DefaultValueProvider;
import de.mirkosertic.gameengine.type.TextExpression;
import de.mirkosertic.gameengine.type.ValueProvider;

import java.util.HashMap;
import java.util.Map;

public class GeExpressionParser implements ExpressionParser {

    private final TextExpression textExpression;
    private final Map<String, ValueProvider> variables;

    GeExpressionParser(TextExpression aExpression) {
        textExpression = aExpression;
        variables = new HashMap<String, ValueProvider>();
    }

    @Override
    public void registerVariable(String aVariableName, PropertyAware aPropertyAware) {
        registerVariable(aVariableName, new DefaultValueProvider(aPropertyAware));
    }

    @Override
    public <T> void registerVariable(String aVariableName, ValueProvider<T> aValueProvider) {
        variables.put(aVariableName, aValueProvider);
    }

    @Override
    public String evaluateToString() {
        return textExpression.expression;
    }

    @Override
    public Object evaluateToObject() {
        return null;
    }
}
