package de.mirkosertic.gameengine.teavm;

import org.teavm.dom.browser.Window;
import org.teavm.jso.JSProperty;

public interface TeaVMWindow extends Window {

    @JSProperty
    int getClientWidth();

    @JSProperty
    int getClientHeight();
}
