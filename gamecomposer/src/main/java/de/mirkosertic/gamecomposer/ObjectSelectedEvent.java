package de.mirkosertic.gamecomposer;

public class ObjectSelectedEvent {

    private final Object selectedObject;

    public ObjectSelectedEvent(Object aObject) {
        selectedObject = aObject;
    }

    public Object getSelectedObject() {
        return selectedObject;
    }
}
