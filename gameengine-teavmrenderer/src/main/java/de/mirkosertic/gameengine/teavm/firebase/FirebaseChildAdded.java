package de.mirkosertic.gameengine.teavm.firebase;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

@JSFunctor
public interface FirebaseChildAdded extends JSObject {

    void handleChildAdded(FirebaseDataSnapshot aChildSnapshot, JSObject aPrevChildName);
}
