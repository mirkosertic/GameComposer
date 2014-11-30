package de.mirkosertic.gameengine.teavm;

import org.teavm.dom.events.Event;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

interface TeaVMKeyEvent extends Event {

    @JSProperty
    JSObject getWhich();

    @JSProperty
    int getKeyCode();
}