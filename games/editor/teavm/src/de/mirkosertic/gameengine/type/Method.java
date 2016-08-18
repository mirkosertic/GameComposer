package de.mirkosertic.gameengine.type;

public abstract class Method<T> {

    private final String name;
    private final Class<?>[] arguments;

    public Method(String aName, Class<?>[] aArguments) {
        name = aName;
        arguments = aArguments;
    }

    public String getName() {
        return name;
    }

    public Class[] getArgument() {
        return arguments;
    }

    public abstract Object invoke(T aTarget, Object[] aArguments);
}
