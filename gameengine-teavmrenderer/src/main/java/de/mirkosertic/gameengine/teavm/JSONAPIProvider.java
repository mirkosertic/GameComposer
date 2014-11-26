package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface JSONAPIProvider extends JSObject {

    @JSProperty("JSON")
    JSONAPI getJSONAPI();
}
