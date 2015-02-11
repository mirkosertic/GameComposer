package de.mirkosertic.gameengine.scriptengine.luaj;

import de.mirkosertic.gameengine.type.KeyValueObjectCache;

import java.util.HashMap;

public class DummyKeyValueObjectCache extends HashMap<Object, Object> implements KeyValueObjectCache {

    @Override
    public <T> T getObjectForKey(Object aKey) {
        return (T) get(aKey);
    }

    @Override
    public <T> void setObjectForKey(Object aKey, T aValue) {
        put(aKey, aValue);
    }
}
