package de.mirkosertic.gameengine.expression;

interface AutomaticResultConverter<T,V> {
    V convert(T aValue);
}

