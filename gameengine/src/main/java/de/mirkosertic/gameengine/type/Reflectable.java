package de.mirkosertic.gameengine.type;

public interface Reflectable<T extends ClassInformation> {

    T getClassInformation();
}
