package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import de.mirkosertic.gameengine.type.Font;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLOptionElement;

public abstract class FontPropertyEditorHTMLElement implements HTMLElement {

    @JSProperty
    public abstract void setLabel1(String aLabel);

    @JSProperty
    public abstract String getLabel1();

    @JSProperty
    public abstract void setLabel2(String aLabel);

    @JSProperty
    public abstract String getLabel2();

    @JSProperty
    public abstract void setIndex(int aValue);

    @JSProperty
    public abstract int getIndex();

    @JSProperty
    public abstract void setSize(String aSize);

    @JSProperty
    public abstract String getSize();

    private <T> int indexOf(T aValue, T[] aValues) {
        for (int i=0;i<aValues.length;i++) {
            T theEntry = aValues[i];
            if (theEntry == aValue) {
                return i;
            }
        }
        return -1;
    }

    public HTMLInputBinder bindTo(Property<Font> aProperty, Font.FontName[] aValues) {

        int theCurrentIndex = indexOf(aProperty.get().name, aValues);

        for (int i=0; i<aValues.length; i++) {
            Font.FontName theValue = aValues[i];
            HTMLOptionElement theOption = (HTMLOptionElement) getOwnerDocument().createElement("option");
            theOption.setInnerHTML(theValue.toString());
            appendChild(theOption);
        }

        setIndex(theCurrentIndex);
        setSize(Integer.toString(aProperty.get().size));

        addEventListener("index-changed", aEvent -> {
            aProperty.set(aProperty.get().changeName(aValues[getIndex()]));
        });
        addEventListener("size-changed", aEvent -> {
            aProperty.set(aProperty.get().changeSize(Integer.parseInt(getSize())));
        });
        GameEventListener<PropertyChanged> theListener = aEvent -> {
            Font theFont = aProperty.get();
            setIndex(indexOf(theFont.name, aValues));
            setSize(Integer.toString(theFont.size));
        };
        aProperty.addChangeListener(theListener);
        return new HTMLInputBinder(theListener, aProperty);
    }
}