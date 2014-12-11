package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface TeaVMTouchEvent extends JSObject {

    @JSProperty("type")
    String getType();

    @JSProperty("touches")
    TeaVMTouchArray getTouches();
}
