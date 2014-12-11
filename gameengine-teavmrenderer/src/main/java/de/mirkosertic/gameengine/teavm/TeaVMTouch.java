package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface TeaVMTouch extends JSObject {

    @JSProperty("identifier")
    int getIdentifier();

    @JSProperty("screenX")
    int getScreenX();

    @JSProperty("screenY")
    int getScreenY();
}