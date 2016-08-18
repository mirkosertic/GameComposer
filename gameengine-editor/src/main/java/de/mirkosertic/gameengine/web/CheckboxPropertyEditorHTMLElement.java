package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class CheckboxPropertyEditorHTMLElement implements HTMLElement {

    @JSProperty
    public abstract void setLabel(String aLabel);

    @JSProperty
    public abstract String getLabel();

    @JSProperty
    public abstract void setValue(boolean aValue);

    @JSProperty
    public abstract boolean getValue();

    public HTMLInputBinder bindTo(Property<Boolean> aProperty) {
        setValue(aProperty.get());
        addEventListener("value-changed", aEvent -> aProperty.set(getValue()));
        GameEventListener<PropertyChanged> theListener = aEvent -> setValue(aProperty.get());
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}
