package de.mirkosertic.gameengine.teavm.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface JSOConstructors extends JSObject {

    @JSProperty("Array")
    JSObject getArray();
}