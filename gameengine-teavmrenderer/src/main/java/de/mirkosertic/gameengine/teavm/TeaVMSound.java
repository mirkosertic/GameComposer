package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public abstract class TeaVMSound implements JSObject {

    public abstract void play();

    @JSBody(
        params = {"aResourceName"},
        script = "return new Audio(aResourceName);"
    )
    public native static TeaVMSound create(String aResourceName);
}
