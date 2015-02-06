package de.mirkosertic.gameengine.teavm;

import org.teavm.dom.core.Element;
import org.teavm.dom.html.HTMLDocument;
import org.teavm.jso.JS;

class TeaVMLogger {

    private static final TeaVMWindow window = (TeaVMWindow) JS.getGlobal();
    private static final HTMLDocument document = window.getDocument();

    public static void info(String aMessage) {
        Element theLogger = document.getElementById("logger");
        Element theDiv = document.createElement("div");
        theDiv.appendChild(document.createTextNode(aMessage));
        theLogger.appendChild(theDiv);
    }

    public static void error(String aMessage) {
        Element theLogger = document.getElementById("logger");
        Element theDiv = document.createElement("div");
        theDiv.setAttribute("style","color: red;");
        theDiv.appendChild(document.createTextNode(aMessage));
        theLogger.appendChild(theDiv);
    }
}
