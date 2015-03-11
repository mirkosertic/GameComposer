package de.mirkosertic.gameengine.teavm.firebase;

import org.teavm.jso.JSConstructor;
import org.teavm.jso.JSObject;

public interface Firebase extends JSObject {

    @JSConstructor("Firebase")
    FirebaseRef create(String aFirebaseURL);
}
