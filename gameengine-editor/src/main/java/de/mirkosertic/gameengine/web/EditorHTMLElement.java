package de.mirkosertic.gameengine.web;

import org.teavm.jso.JSIndexer;
import org.teavm.jso.dom.html.HTMLElement;

public interface EditorHTMLElement extends HTMLElement {

    void scrollIntoView(boolean aParam);

    @JSIndexer
    void set(String aKey, String aValue);

    @JSIndexer
    String get(String aKey);
}
