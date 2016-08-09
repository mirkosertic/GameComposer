package de.mirkosertic.gameengine.web;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLDocument;

public abstract class DOMParser implements JSObject {

    @JSBody(params = {}, script = "return new DOMParser();")
    public static native DOMParser create();

    public abstract HTMLDocument parseFromString(String aValue, String aContentType);
}
