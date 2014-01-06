package de.mirkosertic.gameengine.expression.ge;

class MinFunction implements Function<Number> {

    @Override
    public int getNumberOfArguments() {
        return 2;
    }

    @Override
    public ArgumentType[] getArgumentTypes() {
        return new ArgumentType[] {ArgumentType.NUMBER, ArgumentType.NUMBER};
    }

    @Override
    public Number evaluate(Object[] aArguments) {
        Number theNumber1;
        if (!(aArguments[0] instanceof Number)) {
            throw new IllegalArgumentException("argument 1 is not a number : "+aArguments[0]);
        }
        theNumber1 = (Number) aArguments[0];
        Number theNumber2;
        if (!(aArguments[1] instanceof Number)) {
            throw new IllegalArgumentException("argument 2 is not a number : "+aArguments[1]);
        }
        theNumber2 = (Number) aArguments[1];
        if (theNumber1 instanceof Integer) {
            return Math.min(theNumber1.intValue(), theNumber2.intValue());
        }
        return Math.min(theNumber1.doubleValue(), theNumber2.doubleValue());
    }
}
