package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.type.Color;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class ColorPropertyEditorHTMLElement implements HTMLElement {

    @JSProperty
    public abstract void setLabel(String aLabel);

    @JSProperty
    public abstract String getLabel();

    @JSProperty
    public abstract void setValue(String aValue);

    @JSProperty
    public abstract String getValue();

    public HTMLInputBinder bindTo(Property<Color> aProperty) {
        setValue(CSSUtils.toColorHex(aProperty.get()));
        addEventListener("value-changed", aEvent -> aProperty.set(CSSUtils.fromColorHex(getValue())));
        GameEventListener<PropertyChanged> theListener = aEvent -> setValue(CSSUtils.toColorHex(aProperty.get()));
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}
