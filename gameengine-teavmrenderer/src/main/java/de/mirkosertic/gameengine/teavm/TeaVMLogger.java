package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSBody;

public abstract class TeaVMLogger {

    @JSBody(params = {"aMessage"}, script = "console.info(aMessage);")
    public static native void info(String aMessage);

    @JSBody(params = {"aMessage"}, script = "console.error(aMessage);")
    public static native void error(String aMessage);

    @JSBody(params = {"aLabel"}, script = "console.time(aLabel);")
    public static native void time(String aLabel);

    @JSBody(params = {"aLabel"}, script = "console.timeEnd(aLabel);")
    public static native void timeEnd(String aLabel);
}