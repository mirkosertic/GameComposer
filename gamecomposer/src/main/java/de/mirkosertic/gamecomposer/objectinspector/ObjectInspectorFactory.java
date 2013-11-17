package de.mirkosertic.gamecomposer.objectinspector;

public interface ObjectInspectorFactory<T,V> {

    V create(T aObject);
}
