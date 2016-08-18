package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class TwofieldPropertyEditorHTMLElement implements HTMLElement {

    @JSProperty
    public abstract void setLabel1(String aLabel);

    @JSProperty
    public abstract String getLabel1();

    @JSProperty
    public abstract void setLabel2(String aLabel);

    @JSProperty
    public abstract String getLabe12();

    @JSProperty
    public abstract void setValue1(String aValue);

    @JSProperty
    public abstract String getValue1();

    @JSProperty
    public abstract void setValue2(String aValue);

    @JSProperty
    public abstract String getValue2();

    public <T> HTMLInputBinder bindField1To(Property<T> aProperty, HTMLInputBinder.Converter<T, String> aConverter) {
        setValue1(aConverter.convertFrom(aProperty.get()));
        addEventListener("value1-changed", aEvent -> aProperty.set(aConverter.convertTo(getValue1())));
        GameEventListener<PropertyChanged> theListener = aEvent -> setValue1(aConverter.convertFrom(aProperty.get()));
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }

    public <T> HTMLInputBinder bindField2To(Property<T> aProperty, HTMLInputBinder.Converter<T, String> aConverter) {
        setValue2(aConverter.convertFrom(aProperty.get()));
        addEventListener("value2-changed", aEvent -> aProperty.set(aConverter.convertTo(getValue2())));
        GameEventListener<PropertyChanged> theListener = aEvent -> setValue2(aConverter.convertFrom(aProperty.get()));
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}