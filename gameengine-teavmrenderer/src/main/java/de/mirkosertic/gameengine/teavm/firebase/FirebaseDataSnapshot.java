package de.mirkosertic.gameengine.teavm.firebase;

import org.teavm.jso.JSMethod;
import org.teavm.jso.JSObject;

public interface FirebaseDataSnapshot extends JSObject {

    @JSMethod
    String key();

    @JSMethod()
    JSObject val();
}
