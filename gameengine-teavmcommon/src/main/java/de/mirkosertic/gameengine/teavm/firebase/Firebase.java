package de.mirkosertic.gameengine.teavm.firebase;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public abstract class Firebase implements JSObject {

    @JSBody(
            params = {"aFirebaseURL"},
            script = "return new Firebase(aFirebaseURL);"
    )
    public static native FirebaseRef create(String aFirebaseURL);
}