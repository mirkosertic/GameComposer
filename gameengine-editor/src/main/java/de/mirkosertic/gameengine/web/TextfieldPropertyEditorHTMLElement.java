package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class TextfieldPropertyEditorHTMLElement implements HTMLElement {

    @JSProperty
    public abstract void setLabel(String aLabel);

    @JSProperty
    public abstract String getLabel();

    @JSProperty
    public abstract void setValue(String aValue);

    @JSProperty
    public abstract String getValue();

    public <T> HTMLInputBinder bindTo(Property<T> aProperty, HTMLInputBinder.Converter<T, String> aConverter) {
        setValue(aConverter.convertFrom(aProperty.get()));
        addEventListener("value-changed", aEvent -> aProperty.set(aConverter.convertTo(getValue())));
        GameEventListener<PropertyChanged> theListener = aEvent -> setValue(aConverter.convertFrom(aProperty.get()));
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}
