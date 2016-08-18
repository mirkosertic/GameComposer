package de.mirkosertic.gameengine.web;

import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class GlobalSeparatorHTMLElement implements HTMLElement {

    @JSProperty
    public abstract void setText(String aLabel);

    @JSProperty
    public abstract String getText();

    @JSProperty
    public abstract void setMajor(boolean aValue);

    @JSProperty
    public abstract boolean isMajor();
}