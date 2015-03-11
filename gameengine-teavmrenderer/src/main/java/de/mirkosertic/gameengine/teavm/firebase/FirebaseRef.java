package de.mirkosertic.gameengine.teavm.firebase;

import org.teavm.jso.JSMethod;
import org.teavm.jso.JSObject;

public interface FirebaseRef extends JSObject {

    @JSMethod
    FirebaseRef child(String aValue);

    @JSMethod
    void set(String aJSONValue);

    @JSMethod
    void set(JSObject aObject);

    @JSMethod
    void push(String aJSONValue);

    @JSMethod
    FirebaseRef push(JSObject aObject);

    @JSMethod
    String key();

    @JSMethod
    void on(String aEvent, FirebaseChildAdded aChildAddedEvent);

    @JSMethod
    void remove();
}
