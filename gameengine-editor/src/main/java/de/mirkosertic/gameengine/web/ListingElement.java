package de.mirkosertic.gameengine.web;

import org.teavm.jso.dom.html.HTMLElement;

import java.util.HashSet;
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
        GlobalSeparatorHTMLElement theElement = templateEngine.createNewComponent("global-separator");
        theElement.setText(aTitle);
        theElement.setMajor(true);
        htmlElement.appendChild(theElement);
        return theElement;
    }

    protected HTMLElement addTitleLevel2(String aTitle) {
        GlobalSeparatorHTMLElement theElement = templateEngine.createNewComponent("global-separator");
        theElement.setText(aTitle);
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