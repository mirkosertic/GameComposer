package de.mirkosertic.gamecomposer.contentarea;

public interface ContentAreaFactory<T,V> {

    V create(T aObject);
}
