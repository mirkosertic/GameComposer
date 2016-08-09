package de.mirkosertic.gameengine.teavm.firebase;

import org.teavm.jso.JSMethod;
import org.teavm.jso.JSObject;

public abstract class FirebaseRef implements JSObject {

    @JSMethod
    public abstract FirebaseRef child(String aValue);

    @JSMethod
    public abstract void set(String aJSONValue);

    @JSMethod
    public abstract void set(JSObject aObject);

    @JSMethod
    public abstract void push(String aJSONValue);

    @JSMethod
    public abstract FirebaseRef push(JSObject aObject);

    @JSMethod
    public abstract String key();

    @JSMethod
    public abstract void on(String aEvent, FirebaseChildAdded aChildAddedEvent);

    @JSMethod
    public abstract void remove();
}