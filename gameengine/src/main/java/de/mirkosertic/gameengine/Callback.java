package de.mirkosertic.gameengine;

public interface Callback<T> {

    void process(T aValue);
}