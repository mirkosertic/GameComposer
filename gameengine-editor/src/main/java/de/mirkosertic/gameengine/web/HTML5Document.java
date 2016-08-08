package de.mirkosertic.gameengine.web;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Document;

public interface HTML5Document extends Document {

    @JSBody(params = { "aNode" , "aResolve"}, script = "return this.importNode(aNode, aResolve)")
    HTMLElement importNode(HTMLElement aNode, boolean aResolve );

    @JSBody(params = { "aElement" }, script = "return aElement.innerHTML")
    String toHTML(HTMLElement aElement);
}
