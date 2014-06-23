package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.type.Method;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.Stack;

class RPNEvaluator {

    private final PropertyDiscoverer propertyDiscoverer;
    private final VariableResolver variableResolver;
    private final Reflectable functionRegistry;

    public RPNEvaluator(PropertyDiscoverer aPropertyDiscoverer, VariableResolver aVariableResolver, Reflectable aFunctionRegistry) {
        propertyDiscoverer = aPropertyDiscoverer;
        variableResolver = aVariableResolver;
        functionRegistry = aFunctionRegistry;
    }

    private Object resolve(Object aValue) {
        if (aValue instanceof Token) {
            Token theToken = (Token) aValue;
            switch (theToken.type) {
                case STRING:
                    return theToken.value;
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
                    String theValue = theToken.value;
                    if (theValue.startsWith("-")) {
                        return theValue;
                    }
                    if (theValue.startsWith("+")) {
                        return theValue;
                    }
                    if (Character.isDigit(theValue.charAt(0))) {
                        return theValue;
                    }
                    return variableResolver.resolveVariable(theToken.value);
            }
        }
        return aValue;
    }

    private Object add(Object aObject1, Object aObject2) {
        Number theNumber1 = propertyDiscoverer.convert(aObject1, Number.class);
        Number theNumber2 = propertyDiscoverer.convert(aObject2, Number.class);
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
        Number theNumber1 = propertyDiscoverer.convert(aObject1, Number.class);
        Number theNumber2 = propertyDiscoverer.convert(aObject2, Number.class);
        if (theNumber1 instanceof Integer) {
            return theNumber1.intValue() - theNumber2.intValue();
        }
        return theNumber1.doubleValue() - theNumber2.doubleValue();
    }

    private Object divide(Object aObject1, Object aObject2) {
        Number theNumber1 = propertyDiscoverer.convert(aObject1, Number.class);
        Number theNumber2 = propertyDiscoverer.convert(aObject2, Number.class);
        if (theNumber1 instanceof Integer) {
            return theNumber1.intValue() / theNumber2.intValue();
        }
        return theNumber1.doubleValue() / theNumber2.doubleValue();
    }

    private Object multiply(Object aObject1, Object aObject2) {
        Number theNumber1 = propertyDiscoverer.convert(aObject1, Number.class);
        Number theNumber2 = propertyDiscoverer.convert(aObject2, Number.class);
        if (theNumber1 instanceof Integer) {
            return theNumber1.intValue() * theNumber2.intValue();
        }
        return theNumber1.doubleValue() * theNumber2.doubleValue();
    }

    public Object evaluate(Token[] aRPNTokens) {
        Stack<Object> theResultStack = new Stack<>();
        for (int i = 0; i < aRPNTokens.length; i++) {
            Token theToken = aRPNTokens[i];
            if (theToken.type == TokenType.VALUE || theToken.type == TokenType.STRING) {
                theResultStack.push(theToken);
            }
            if (theToken.type == TokenType.FUNCTION) {
                Method theMethod = functionRegistry.getClassInformation().getMethodByName(theToken.value);
                if (theMethod == null) {
                    throw new IllegalArgumentException("Unknown function : " + theToken.value);
                }
                Class[] theArgumentTypes = theMethod.getArgument();

                Object[] theArguments = new Object[theArgumentTypes.length];
                for (int theArgumentIndex = theArgumentTypes.length - 1; theArgumentIndex >= 0; theArgumentIndex--) {
                    theArguments[theArgumentIndex] = propertyDiscoverer.convert((resolve(theResultStack.pop())), theArgumentTypes[theArgumentIndex]);
                }
                theResultStack.push(theMethod.invoke(functionRegistry, theArguments));
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
