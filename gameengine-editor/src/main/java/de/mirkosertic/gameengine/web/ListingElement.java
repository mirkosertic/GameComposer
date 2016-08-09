package de.mirkosertic.gameengine.web;

import org.teavm.jso.dom.html.HTMLElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ListingElement {

    protected final HTMLElement htmlElement;
    protected final HTMLTemplateEngine templateEngine;
    protected final Set<HTMLInputBinder> binder;

    public ListingElement(HTMLElement aHtmlElement, HTMLTemplateEngine aTemplateEngine) {
        htmlElement = aHtmlElement;
        templateEngine = aTemplateEngine;
        binder = new HashSet<>();
    }

    protected HTMLElement addTitleLevel1(String aTitle) {
        Map<String, Object> theParams = new HashMap<>();
        theParams.put("title", aTitle);
        HTMLElement theElement = templateEngine.renderToElement("majorSeparator", theParams);
        htmlElement.appendChild(theElement);
        return theElement;
    }

    protected HTMLElement addTitleLevel2(String aTitle) {
        Map<String, Object> theParams = new HashMap<>();
        theParams.put("title", aTitle);
        HTMLElement theElement = templateEngine.renderToElement("minorSeparator", theParams);
        htmlElement.appendChild(theElement);
        return theElement;
    }

    protected void clear() {
        for (HTMLInputBinder theBinder : binder) {
            theBinder.unbind();
        }
        htmlElement.clear();
    }
}
