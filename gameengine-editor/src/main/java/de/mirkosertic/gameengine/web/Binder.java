package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import org.teavm.jso.dom.html.HTMLInputElement;

public class Binder {

    public interface Converter<T> {

        String asString(T aValue);

        T asValue(String aValue);
    }

    private final GameEventListener<PropertyChanged> listener;
    private final Property property;

    public static Binder forStringProperty(HTMLInputElement aElement, Property<String> aProperty) {
        GameEventListener<PropertyChanged> theListener = aEvent -> aElement.setValue(aProperty.get());
        aProperty.addChangeListener(theListener);
        aElement.setValue(aProperty.get());
        aElement.addEventListener("blur", evt -> aProperty.set(aElement.getValue()));
        return new Binder(theListener, aProperty);
    }

    public static Binder forBooleanProperty(HTMLInputElement aElement, Property<Boolean> aProperty) {
        GameEventListener<PropertyChanged> theListener = aEvent -> aElement.setChecked(aProperty.get());
        aProperty.addChangeListener(theListener);
        aElement.setChecked(aProperty.get());
        aElement.addEventListener("change", evt -> aProperty.set(aElement.isChecked()));
        return new Binder(theListener, aProperty);
    }

    public static <T> Binder forAnyProperty(HTMLInputElement aElement, Property<T> aProperty, Converter<T> aConverter) {
        GameEventListener<PropertyChanged> theListener = aEvent -> aElement.setValue(aConverter.asString(aProperty.get()));
        aProperty.addChangeListener(theListener);
        aElement.setValue(aConverter.asString(aProperty.get()));
        aElement.addEventListener("change", evt -> aProperty.set(aConverter.asValue(aElement.getValue())));
        return new Binder(theListener, aProperty);
    }

    private Binder(GameEventListener<PropertyChanged> listener, Property property) {
        this.listener = listener;
        this.property = property;
    }

    public void unbind() {
        property.removeChangeListener(listener);
    }
}
