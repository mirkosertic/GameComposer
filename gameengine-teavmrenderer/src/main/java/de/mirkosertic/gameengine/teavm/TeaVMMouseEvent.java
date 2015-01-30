package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface TeaVMMouseEvent extends JSObject {

    @JSProperty("clientX")
    int getClientX();

    @JSProperty("clientY")
    int getClientY();
}
