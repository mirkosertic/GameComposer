package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class Style implements JSObject {

    @JSProperty
    public abstract void setFontFamily(String aFontFamily);

    @JSProperty
    public abstract void setFontSize(String aFontFamily);

    @JSProperty
    public abstract void setFontStyle(String aFontFamily);

    @JSProperty
    public abstract void setFill(String aCSSColor);

    @JSProperty
    public abstract void setStroke(String aCSSColor);
}
