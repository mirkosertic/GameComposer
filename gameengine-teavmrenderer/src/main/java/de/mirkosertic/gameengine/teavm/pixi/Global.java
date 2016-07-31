package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public class Global {

    @JSBody(params = "aBackgroundColor", script = "return new PIXI.Stage(aBackgroundColor);")
    public static native Stage createStage(int aBackgroundColor);

    @JSBody(params = {"aWidth", "aHeight", "aCanvasElement"}, script = "return PIXI.autoDetectRenderer(\n" +
            "    aWidth,\n" +
            "    aHeight," +
            "    {" +
            "       view: aCanvasElement" +
            "    } "+
            "  );")
    public static native Renderer autodetectRenderer(int aWidth, int aHeight, HTMLCanvasElement aCanvasElement);
}
