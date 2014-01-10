package de.mirkosertic.gameengine.expression;

public interface Function<T> {

    int getNumberOfArguments();

    ArgumentType[] getArgumentTypes();

    T evaluate(Object... aArguments);
}
