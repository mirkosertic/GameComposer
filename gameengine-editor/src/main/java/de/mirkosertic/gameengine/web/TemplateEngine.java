package de.mirkosertic.gameengine.web;

import org.teavm.jso.dom.html.HTMLElement;

import java.util.Map;

public class TemplateEngine {

    private final HTML5Document document;

    public TemplateEngine(HTML5Document aDocument) {
        document = aDocument;
    }

    public HTMLElement renderToElement(String aTemplateID, Map<String, Object> aData) {
        HTMLElement theTemplate = (HTMLElement) document.getElementById(aTemplateID);
        HTMLElement theImportedNode = document.importNode(theTemplate, true);

        String theContent = document.toHTML(theImportedNode);
        for (Map.Entry<String, Object> theEntry : aData.entrySet()) {
            if (theEntry.getValue() != null) {
                theContent = theContent.replace("{{"+theEntry.getKey()+"}}", theEntry.getValue().toString());
            } else {
                theContent = theContent.replace("{{"+theEntry.getKey()+"}}", "");
            }
        }

        DOMParser theParser = DOMParser.create();
        HTMLElement theResult = theParser.parseFromString(theContent, "text/html");
        return (HTMLElement) theResult.getFirstChild();
    }
}
