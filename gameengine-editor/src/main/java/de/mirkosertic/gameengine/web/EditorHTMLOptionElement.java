package de.mirkosertic.gameengine.web;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLOptionElement;

public abstract class EditorHTMLOptionElement implements HTMLOptionElement {

    @JSBody(params = {"aText"}, script = "return new Option(aText);")
    public static native EditorHTMLOptionElement create(String aText);
}
