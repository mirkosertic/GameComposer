package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.core.ExpressionParser;
import de.mirkosertic.gameengine.type.DefaultValueProvider;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.ValueProvider;

import java.util.HashMap;
import java.util.Map;

public class GeExpressionParser implements ExpressionParser, VariableResolver {

    private final Map<String, ValueProvider> variables;
    private final Token[] rpnTokens;
    private final Reflectable functionRegistry;
    private final PropertyDiscoverer propertyDiscoverer;

    GeExpressionParser(Token[] aRPNTokens, Reflectable aFunctionRegistry) {
        rpnTokens = aRPNTokens;
        variables = new HashMap<>();
        functionRegistry = aFunctionRegistry;
        propertyDiscoverer = new PropertyDiscoverer();
    }

    @Override
    public <T> void registerVariable(String aVariableName, ValueProvider<T> aVariableValueProvider) {
        variables.put(aVariableName, aVariableValueProvider);
    }

    @Override
    public <T> void registerVariable(String aVariableName, T aValue) {
        variables.put(aVariableName, new DefaultValueProvider(aValue));
    }

    @Override
    public String evaluateToString() {
        Object theResult = evaluateToObject();
        if (theResult != null) {
            return theResult.toString();
        }
        return null;
    }

    @Override
    public Object evaluateToObject() {
        RPNEvaluator theEvaluator = new RPNEvaluator(propertyDiscoverer, this, functionRegistry);
        return theEvaluator.evaluate(rpnTokens);
    }

    @Override
    public Object resolveVariable(String aVariableName) {
        return propertyDiscoverer.resolveVariable(variables, aVariableName);
    }
}
