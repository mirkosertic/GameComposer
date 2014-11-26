package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSObject;

public interface JSONAPI extends JSObject {

    JSObject parse(String aJSONString);
}