package de.mirkosertic.gameengine.type;

public interface KeyValueObjectCache {

    <T> T getObjectForKey(Object aKey);

    <T> void setObjectForKey(Object aKey, T aValue);
}
