package de.mirkosertic.gamecomposer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChangeEvent;

public class PropertyBinder {

    public static interface Converter<T, V> {
        V beanToUI(T aValue);
        T uiToBean(V aValue);
    }

    public static <T> void bind(final Property<T> aBeanProperty, final javafx.beans.property.Property<T> aFXProperty) {
        aBeanProperty.addChangeListener(new GameEventListener<PropertyChangeEvent>() {
            @Override
            public void handleGameEvent(PropertyChangeEvent aEvent) {
                aFXProperty.setValue((T) aEvent.getNewValue());
            }
        });
        aFXProperty.addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observableValue, T aOldValue, T aNewValue) {
                aBeanProperty.setQuietly(aNewValue);
            }
        });
        aFXProperty.setValue(aBeanProperty.get());
    }

    public static <T, V> void bind(final Property<T> aBeanProperty, final javafx.beans.property.Property<V> aFXProperty, final Converter<T, V> aConverter) {
        aBeanProperty.addChangeListener(new GameEventListener<PropertyChangeEvent>() {
            @Override
            public void handleGameEvent(PropertyChangeEvent aEvent) {
                aFXProperty.setValue(aConverter.beanToUI((T) aEvent.getNewValue()));
            }
        });
        aFXProperty.addListener(new ChangeListener<V>() {
            @Override
            public void changed(ObservableValue<? extends V> observableValue, V aOldValue, V aNewValue) {
                aBeanProperty.setQuietly(aConverter.uiToBean(aNewValue));
            }
        });
        aFXProperty.setValue(aConverter.beanToUI(aBeanProperty.get()));
    }    
}
