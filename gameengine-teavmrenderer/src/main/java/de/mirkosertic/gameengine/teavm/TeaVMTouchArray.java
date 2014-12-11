package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface TeaVMTouchArray extends JSObject {

    @JSProperty("length")
    int getLength();

    @JSIndexer
    TeaVMTouch get(int index);
}
