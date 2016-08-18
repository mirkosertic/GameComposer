package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class TreeItemHTMLElement implements HTMLElement {

    @JSProperty
    public abstract void setText(String aLabel);

    @JSProperty
    public abstract String getText();

    @JSProperty
    public abstract void setLevel(int aValue);

    @JSProperty
    public abstract int getLevel();

    @JSProperty
    public abstract void setSelected(boolean aValue);

    @JSProperty
    public abstract boolean isSelected();

    @JSProperty
    public abstract void setDraggable(boolean aValue);

    @JSProperty
    public abstract boolean isDraggable();

    @JSProperty
    public abstract void setSeparator(boolean aValue);

    @JSProperty
    public abstract boolean isSeparator();

    public abstract void scrollIntoView(boolean aParam);

    public HTMLInputBinder bindTo(Property<String> aProperty) {
        setText(aProperty.get());
        GameEventListener<PropertyChanged> theListener = aEvent -> setText(aProperty.get());
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}
