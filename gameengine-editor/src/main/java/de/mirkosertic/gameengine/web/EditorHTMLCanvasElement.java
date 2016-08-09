package de.mirkosertic.gameengine.web;

import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public interface EditorHTMLCanvasElement extends HTMLCanvasElement {

    @JSProperty
    int getOffsetLeft();

    @JSProperty
    int getOffsetTop();
}
