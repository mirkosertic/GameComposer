package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChangeEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.HashSet;
import java.util.Set;

public class PropertyBinder {

    public static interface Converter<T, V> {
        V beanToUI(T aValue);
        T uiToBean(V aValue);
    }

    private static interface JavaFXPropertyChangeListener extends GameEventListener<PropertyChangeEvent> {

    }

    public static <T> void bind(final Property<T> aBeanProperty, final javafx.beans.property.Property<T> aFXProperty) {
        aBeanProperty.addChangeListener(new JavaFXPropertyChangeListener() {
            @Override
            public void handleGameEvent(PropertyChangeEvent aEvent) {
                aFXProperty.setValue((T) aEvent.getNewValue());
            }
        });
        aFXProperty.addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observableValue, T aOldValue, T aNewValue) {
                aBeanProperty.set(aNewValue);
            }
        });
        aFXProperty.setValue(aBeanProperty.get());
    }

    public static <T, V> void bind(final Property<T> aBeanProperty, final javafx.beans.property.Property<V> aFXProperty, final Converter<T, V> aConverter) {
        aBeanProperty.addChangeListener(new JavaFXPropertyChangeListener() {
            @Override
            public void handleGameEvent(PropertyChangeEvent aEvent) {
                aFXProperty.setValue(aConverter.beanToUI((T) aEvent.getNewValue()));
            }
        });
        aFXProperty.addListener(new ChangeListener<V>() {
            @Override
            public void changed(ObservableValue<? extends V> observableValue, V aOldValue, V aNewValue) {
                aBeanProperty.set(aConverter.uiToBean(aNewValue));
            }
        });
        aFXProperty.setValue(aConverter.beanToUI(aBeanProperty.get()));
    }

    public static void unbind(Property aProperty) {
        Set<GameEventListener<PropertyChangeEvent>> theRegistered = new HashSet<GameEventListener<PropertyChangeEvent>>(aProperty.getChangeListener());
        for (GameEventListener<PropertyChangeEvent> theListener : theRegistered) {
            if (theListener instanceof JavaFXPropertyChangeListener) {
                aProperty.removeChangeListener(theListener);
            }
        }
    }
}
