package de.mirkosertic.gameengine.teavm.howler;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public abstract class Howl implements JSObject {

    @JSBody(params = {"aConfig"}, script = "return new Howl(aConfig);")
    public static native Howl createHowl(Config aConfig);

    public abstract SoundID play();

    public abstract void stop(SoundID aSoundID);
}
