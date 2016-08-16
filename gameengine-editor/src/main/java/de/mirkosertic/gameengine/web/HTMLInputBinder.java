package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.html.HTMLOptionElement;
import org.teavm.jso.dom.html.HTMLSelectElement;

public class HTMLInputBinder {

    public interface Converter<T, V> {

        V convertFrom(T aValue);

        T convertTo(V aValue);
    }

    private final GameEventListener<PropertyChanged> listener;
    private final Property property;

    public static HTMLInputBinder forStringProperty(HTMLInputElement aElement, Property<String> aProperty) {
        GameEventListener<PropertyChanged> theListener = aEvent -> aElement.setValue(aProperty.get());
        aProperty.addChangeListener(theListener);
        aElement.setValue(aProperty.get());
        aElement.addEventListener("blur", evt -> aProperty.set(aElement.getValue()));
        return new HTMLInputBinder(theListener, aProperty);
    }

    public static HTMLInputBinder forElementContent(HTMLElement aElement, Property<String> aProperty) {
        GameEventListener<PropertyChanged> theListener = aEvent -> aElement.setInnerHTML(aProperty.get());
        aProperty.addChangeListener(theListener);
        aElement.setInnerHTML(aProperty.get());
        return new HTMLInputBinder(theListener, aProperty);
    }

    public static HTMLInputBinder forBooleanProperty(HTMLInputElement aElement, Property<Boolean> aProperty) {
        GameEventListener<PropertyChanged> theListener = aEvent -> aElement.setChecked(aProperty.get());
        aProperty.addChangeListener(theListener);
        aElement.setChecked(aProperty.get());
        aElement.addEventListener("change", evt -> aProperty.set(aElement.isChecked()));
        return new HTMLInputBinder(theListener, aProperty);
    }

    public static <T> HTMLInputBinder forAnyProperty(HTMLInputElement aElement, Property<T> aProperty, Converter<T, String> aConverter) {
        GameEventListener<PropertyChanged> theListener = aEvent -> aElement.setValue(aConverter.convertFrom(aProperty.get()));
        aProperty.addChangeListener(theListener);
        aElement.setValue(aConverter.convertFrom(aProperty.get()));
        aElement.addEventListener("change", evt -> aProperty.set(aConverter.convertTo(aElement.getValue())));
        return new HTMLInputBinder(theListener, aProperty);
    }

    public static <T> HTMLInputBinder forAnyProperty(HTMLSelectElement aElement, Property<T> aProperty, T[] aValues) {
        GameEventListener<PropertyChanged> theListener = aEvent -> aElement.setSelectedIndex(aElement.getOptions().namedItem(aProperty.get().toString()).getIndex());
        aProperty.addChangeListener(theListener);
        aElement.setSelectedIndex(aElement.getOptions().namedItem(aProperty.get().toString()).getIndex());
        aElement.addEventListener("change", evt -> {
            int theIndex = aElement.getSelectedIndex();
            if (theIndex >= 0) {
                aProperty.set(aValues[theIndex]);
            }
        });
        return new HTMLInputBinder(theListener, aProperty);
    }

    public static <T,V> HTMLInputBinder forAnyProperty(HTMLSelectElement aElement, Property<T> aProperty, V[] aValues, Converter<T, V> aConverter) {
        GameEventListener<PropertyChanged> theListener = aEvent -> {
            T theCurrentValue = aProperty.get();
            V theValueToSelect = aConverter.convertFrom(theCurrentValue);
            HTMLOptionElement theElementToSelect = aElement.getOptions().namedItem(theValueToSelect.toString());
            aElement.setSelectedIndex(theElementToSelect.getIndex());
        };
        aProperty.addChangeListener(theListener);
        aElement.setSelectedIndex(aElement.getOptions().namedItem(aConverter.convertFrom(aProperty.get()).toString()).getIndex());
        aElement.addEventListener("change", evt -> {
            int theIndex = aElement.getSelectedIndex();
            if (theIndex >= 0) {
                aProperty.set(aConverter.convertTo(aValues[theIndex]));
            }
        });
        return new HTMLInputBinder(theListener, aProperty);
    }

    private HTMLInputBinder(GameEventListener<PropertyChanged> aListener, Property aProperty) {
        listener = aListener;
        property = aProperty;
    }

    public void unbind() {
        property.removeChangeListener(listener);
    }
}
