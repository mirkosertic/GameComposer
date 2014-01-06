package de.mirkosertic.gameengine.expression.ge;

public interface Function<T> {

    int getNumberOfArguments();

    ArgumentType[] getArgumentTypes();

    T evaluate(Object... aArguments);
}
