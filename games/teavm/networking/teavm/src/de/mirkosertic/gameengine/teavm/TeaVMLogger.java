package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.browser.Window;

class TeaVMLogger {

    private static final HTMLDocument document = Window.current().getDocument();

    public static void info(String aMessage) {
        HTMLElement theLogger = document.getElementById("logger");
        HTMLElement theDiv = document.createElement("div");
        theDiv.appendChild(document.createTextNode(aMessage));
        theLogger.appendChild(theDiv);
    }

    public static void error(String aMessage) {
        HTMLElement theLogger = document.getElementById("logger");
        HTMLElement theDiv = document.createElement("div");
        theDiv.setAttribute("style","color: red;");
        theDiv.appendChild(document.createTextNode(aMessage));
        theLogger.appendChild(theDiv);
    }
}