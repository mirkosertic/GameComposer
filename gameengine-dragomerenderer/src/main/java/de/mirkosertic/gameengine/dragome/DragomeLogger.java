package de.mirkosertic.gameengine.dragome;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.dragome.web.html.dom.DomHandler;
import com.dragome.web.html.dom.w3c.BrowserDomHandler;

public class DragomeLogger {

    public static void info(String aMessage) {
        DomHandler theDomhandler = new BrowserDomHandler();
        Document theDocument = theDomhandler.getDocument();
        Element theLogger = theDomhandler.getElementBySelector("#logger");

        Element theDiv = theDocument.createElement("div");
        theDiv.appendChild(theDocument.createTextNode(aMessage));
        theLogger.appendChild(theDiv);
    }

    public static void error(String aMessage) {
        DomHandler theDomhandler = new BrowserDomHandler();
        Document theDocument = theDomhandler.getDocument();
        Element theLogger = theDomhandler.getElementBySelector("#logger");

        Element theDiv = theDocument.createElement("div");
        theDiv.setAttribute("style","color: red;");
        theDiv.appendChild(theDocument.createTextNode(aMessage));
        theLogger.appendChild(theDiv);
    }
}