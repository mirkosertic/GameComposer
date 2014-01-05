package de.mirkosertic.gameengine.type;

public class DefaultValueProvider<T> implements ValueProvider<T> {

    private final T value;

    public DefaultValueProvider(T aValue) {
        value = aValue;
    }

    @Override
    public T get() {
        return value;
    }
}
