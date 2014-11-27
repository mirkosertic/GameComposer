package de.mirkosertic.gameengine.teavm.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface JSONAPIProvider extends JSObject {

    @JSProperty("JSON")
    JSONAPI getJSONAPI();
}
