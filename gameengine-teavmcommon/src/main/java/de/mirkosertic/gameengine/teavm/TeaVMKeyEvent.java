package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.events.Event;

interface TeaVMKeyEvent extends Event {

    @JSProperty
    JSObject getWhich();

    @JSProperty
    int getKeyCode();
}