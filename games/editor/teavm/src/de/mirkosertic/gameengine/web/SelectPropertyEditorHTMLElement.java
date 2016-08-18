package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLOptionElement;

public abstract class SelectPropertyEditorHTMLElement implements HTMLElement {

    @JSProperty
    public abstract void setLabel(String aLabel);

    @JSProperty
    public abstract String getLabel();

    @JSProperty
    public abstract void setIndex(int aValue);

    @JSProperty
    public abstract int getIndex();

    @JSBody(params = {"aElement"}, script = "Polymer.dom(this).appendChild(aElement);")
    public abstract void addOption(HTMLOptionElement aElement);

    private <T> int indexOf(T aValue, T[] aValues) {
        for (int i=0;i<aValues.length;i++) {
            T theEntry = aValues[i];
            if (theEntry == aValue) {
                return i;
            }
        }
        return -1;
    }

    public <T> HTMLInputBinder bindTo(Property<T> aProperty, T[] aValues) {

        int theCurrentIndex = indexOf(aProperty.get(), aValues);

        for (int i=0; i<aValues.length; i++) {
            T theValue = aValues[i];

            HTMLOptionElement theOption = (HTMLOptionElement) getOwnerDocument().createElement("option");
            theOption.setInnerHTML(theValue.toString());
            addOption(theOption);
        }

        setIndex(theCurrentIndex);
        addEventListener("index-changed", aEvent -> aProperty.set(aValues[getIndex()]));
        GameEventListener<PropertyChanged> theListener = aEvent -> setIndex(indexOf(aProperty.get(), aValues));
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}