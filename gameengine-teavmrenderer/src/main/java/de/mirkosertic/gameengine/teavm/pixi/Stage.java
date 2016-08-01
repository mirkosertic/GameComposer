package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;

public abstract class Stage extends Container {

    @JSBody(params = {"aBackgroundColor"}, script = "return new PIXI.Stage(aBackgroundColor);")
    public static native Stage createStage(int aBackgroundColor);
}