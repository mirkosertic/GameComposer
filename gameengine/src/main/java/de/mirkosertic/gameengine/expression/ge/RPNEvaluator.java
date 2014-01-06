package de.mirkosertic.gameengine.expression.ge;

import java.util.Stack;

class RPNEvaluator {

    private final FunctionRegistry functionRegistry;
    private final VariableResolver variableResolver;

    public RPNEvaluator(VariableResolver aVariableResolver, FunctionRegistry aFunctionRegistry) {
        variableResolver = aVariableResolver;
        functionRegistry = aFunctionRegistry;
    }

    private Object resolve(Object aValue) {
        if (aValue instanceof Token) {
            Token theToken = (Token) aValue;
            switch (theToken.type) {
                case VALUE:
                    if ("true".equalsIgnoreCase(theToken.value)) {
                        return Boolean.TRUE;
                    }
                    if ("false".equalsIgnoreCase(theToken.value)) {
                        return Boolean.FALSE;
                    }
                    if ("pi".equalsIgnoreCase(theToken.value)) {
                        return Math.PI;
                    }
                    Object theResult = variableResolver.resolveVariable(theToken.value);
                    if (theResult != null) {
                        return theResult;
                    }
                    return theToken.value;
            }
        }
        return aValue;
    }

    private Object add(Object aObject1, Object aObject2) {
        Number theNumber1 = ArgumentType.NUMBER.convert(aObject1);
        Number theNumber2 = ArgumentType.NUMBER.convert(aObject2);
        if (theNumber1 instanceof Integer) {
            return theNumber1.intValue() + theNumber2.intValue();
        }
        return theNumber1.doubleValue() + theNumber2.doubleValue();
    }

    private Object concatenate(Object aObject1, Object aObject2) {
        Object theObject1 = resolve(aObject1);
        Object theObject2 = resolve(aObject2);

        return theObject1.toString() + theObject2;
    }

    private Object substract(Object aObject1, Object aObject2) {
        Number theNumber1 = ArgumentType.NUMBER.convert(aObject1);
        Number theNumber2 = ArgumentType.NUMBER.convert(aObject2);
        if (theNumber1 instanceof Integer) {
            return theNumber1.intValue() - theNumber2.intValue();
        }
        return theNumber1.doubleValue() - theNumber2.doubleValue();
    }

    private Object divide(Object aObject1, Object aObject2) {
        Number theNumber1 = ArgumentType.NUMBER.convert(aObject1);
        Number theNumber2 = ArgumentType.NUMBER.convert(aObject2);
        if (theNumber1 instanceof Integer) {
            return theNumber1.intValue() / theNumber2.intValue();
        }
        return theNumber1.doubleValue() / theNumber2.doubleValue();
    }

    private Object multiply(Object aObject1, Object aObject2) {
        Number theNumber1 = ArgumentType.NUMBER.convert(aObject1);
        Number theNumber2 = ArgumentType.NUMBER.convert(aObject2);
        if (theNumber1 instanceof Integer) {
            return theNumber1.intValue() * theNumber2.intValue();
        }
        return theNumber1.doubleValue() * theNumber2.doubleValue();
    }

    public Object evaluate(Token[] aRPNTokens) {
        Stack<Object> theResultStack = new Stack<Object>();
        for (int i = 0; i < aRPNTokens.length; i++) {
            Token theToken = aRPNTokens[i];
            if (theToken.type == TokenType.VALUE) {
                theResultStack.push(theToken);
            }
            if (theToken.type == TokenType.FUNCTION) {
                Function theFunction = functionRegistry.resolveFunctionByName(theToken.value);
                if (theFunction == null) {
                    throw new IllegalArgumentException("Unknown function : " + theToken.value);
                }
                ArgumentType[] theArgumentTypes = theFunction.getArgumentTypes();

                Object[] theArguments = new Object[theFunction.getNumberOfArguments()];
                for (int theArgumentIndex = theFunction.getNumberOfArguments() - 1; theArgumentIndex >= 0; theArgumentIndex--) {
                    theArguments[theArgumentIndex] = theArgumentTypes[theArgumentIndex].convert(resolve(theResultStack.pop()));
                }
                theResultStack.push(theFunction.evaluate(theArguments));
            }
            if (theToken.type == TokenType.ADD) {
                Object theValue2 = resolve(theResultStack.pop());
                Object theValue1 = resolve(theResultStack.pop());
                theResultStack.push(add(theValue1, theValue2));
            }
            if (theToken.type == TokenType.STRINGCONCATENATE) {
                Object theValue2 = resolve(theResultStack.pop());
                Object theValue1 = resolve(theResultStack.pop());
                theResultStack.push(concatenate(theValue1, theValue2));
            }
            if (theToken.type == TokenType.SUBSTRACT) {
                Object theValue2 = resolve(theResultStack.pop());
                Object theValue1 = resolve(theResultStack.pop());
                theResultStack.push(substract(theValue1, theValue2));
            }
            if (theToken.type == TokenType.DIVIDE) {
                Object theValue2 = resolve(theResultStack.pop());
                Object theValue1 = resolve(theResultStack.pop());
                theResultStack.push(divide(theValue1, theValue2));
            }
            if (theToken.type == TokenType.MULTIPLY) {
                Object theValue2 = resolve(theResultStack.pop());
                Object theValue1 = resolve(theResultStack.pop());
                theResultStack.push(multiply(theValue1, theValue2));
            }
        }
        if (theResultStack.size() != 1) {
            throw new IllegalArgumentException("Invalid stack size : " + theResultStack);
        }
        return resolve(theResultStack.pop());
    }
}
