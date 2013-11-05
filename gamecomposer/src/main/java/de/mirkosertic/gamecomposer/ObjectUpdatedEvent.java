package de.mirkosertic.gamecomposer;

public class ObjectUpdatedEvent {

    private Object object;

    public ObjectUpdatedEvent(Object aObject) {
        object = aObject;
    }

    public Object getObject() {
        return object;
    }
}
