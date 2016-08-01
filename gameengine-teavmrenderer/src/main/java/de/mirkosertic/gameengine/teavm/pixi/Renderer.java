package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public abstract class Renderer implements JSObject {

    @JSBody(params = { "aWidth", "aHeight", "aCanvasElement" }, script = "return PIXI.autoDetectRenderer(\n" +
            "    aWidth,\n" +
            "    aHeight," +
            "    {" +
            "       view: aCanvasElement" +
            "    } " +
            "  );")
    public static native Renderer autodetectRenderer(int aWidth, int aHeight, HTMLCanvasElement aCanvasElement);

    @JSBody(params = { "aWidth", "aHeight", "aCanvasElement" }, script = "return new PIXI.CanvasRenderer(\n" +
            "    aWidth,\n" +
            "    aHeight," +
            "    {" +
            "       view: aCanvasElement" +
            "    } " +
            "  );")
    public static native Renderer canvasRenderer(int aWidth, int aHeight, HTMLCanvasElement aCanvasElement);

    public abstract void render(Container aStage);

    @JSProperty
    public abstract void setBackgroundColor(int aColor);

    public abstract void resize(int aWidth, int aHeight);
}
