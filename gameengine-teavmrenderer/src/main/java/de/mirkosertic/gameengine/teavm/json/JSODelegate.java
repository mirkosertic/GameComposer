package de.mirkosertic.gameengine.teavm.json;

import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface JSODelegate extends JSObject {
    @JSIndexer
    JSObject get(String aIndex);

    @JSProperty
    JSObject getConstructor();
}