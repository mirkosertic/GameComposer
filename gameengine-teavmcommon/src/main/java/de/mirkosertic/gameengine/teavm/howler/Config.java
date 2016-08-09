package de.mirkosertic.gameengine.teavm.howler;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class Config implements JSObject {

    @JSBody(params = {}, script = "return {};")
    public static native Config createConfig();

    @JSProperty
    public abstract void setSrc(String[] aSources);

}
