package de.mirkosertic.gameengine.web;

import org.teavm.jso.dom.html.HTMLElement;

public class HTMLTemplateEngine {

    private final HTML5Document document;

    public HTMLTemplateEngine(HTML5Document aDocument) {
        document = aDocument;
    }

    public <T extends HTMLElement> T createNewComponent(String aComponentID) {
        return (T) document.createElement(aComponentID);
    }
}
