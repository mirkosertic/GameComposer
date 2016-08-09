package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArrayReader;

public interface TeaVMTouchEvent extends JSObject {

    @JSProperty("type")
    String getType();

    @JSProperty("touches")
    JSArrayReader<TeaVMTouch> getTouches();
}
