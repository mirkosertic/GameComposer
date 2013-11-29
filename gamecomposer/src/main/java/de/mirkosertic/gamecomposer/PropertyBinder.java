package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import java.util.HashSet;
import java.util.Set;

public class PropertyBinder {

    public static interface Converter<T, V> {
        V beanToUI(T aValue);
        T uiToBean(V aValue);
    }

    private static interface JavaFXPropertyChangeListener extends GameEventListener<PropertyChanged> {

    }

    public static <T> void bindUIToBean(final Property<T> aBeanProperty, final javafx.beans.property.Property<T> aFXProperty) {
        Node theNode = (Node) aFXProperty.getBean();
        theNode.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aOldValue, Boolean aNewValue) {
                if (aOldValue && !aNewValue) {
                    aBeanProperty.set(aFXProperty.getValue());
                }
            }
        });
        aFXProperty.setValue(aBeanProperty.get());
    }

    public static <T, V> void bindUIToBean(final Property<T> aBeanProperty, final javafx.beans.property.Property<V> aFXProperty, final Converter<T, V> aConverter) {
        Node theNode = (Node) aFXProperty.getBean();
        theNode.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aOldValue, Boolean aNewValue) {
                if (aOldValue && !aNewValue) {
                    aBeanProperty.set(aConverter.uiToBean(aFXProperty.getValue()));
                }
            }
        });
        aFXProperty.setValue(aConverter.beanToUI(aBeanProperty.get()));
    }

    public static <T> void bind(final Property<T> aBeanProperty, final javafx.beans.property.Property<T> aFXProperty) {
        aBeanProperty.addChangeListener(new JavaFXPropertyChangeListener() {
            @Override
            public void handleGameEvent(PropertyChanged aEvent) {
                aFXProperty.setValue((T) aEvent.propertyProperty().get().get());
            }
        });
        Node theNode = (Node) aFXProperty.getBean();
        theNode.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aOldValue, Boolean aNewValue) {
                if (aOldValue && !aNewValue) {
                    aBeanProperty.set(aFXProperty.getValue());
                }
            }
        });
        aFXProperty.setValue(aBeanProperty.get());
    }

    public static <T, V> void bind(final Property<T> aBeanProperty, final javafx.beans.property.Property<V> aFXProperty, final Converter<T, V> aConverter) {
        aBeanProperty.addChangeListener(new JavaFXPropertyChangeListener() {
            @Override
            public void handleGameEvent(PropertyChanged aEvent) {
                aFXProperty.setValue(aConverter.beanToUI((T) aEvent.propertyProperty().get().get()));
            }
        });
        Node theNode = (Node) aFXProperty.getBean();
        theNode.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aOldValue, Boolean aNewValue) {
                if (aOldValue && !aNewValue) {
                    aBeanProperty.set(aConverter.uiToBean(aFXProperty.getValue()));
                }
            }
        });
        aFXProperty.setValue(aConverter.beanToUI(aBeanProperty.get()));
    }

    public static void unbind(Property aProperty) {
        Set<GameEventListener<PropertyChanged>> theRegistered = new HashSet<GameEventListener<PropertyChanged>>(aProperty.getChangeListener());
        for (GameEventListener<PropertyChanged> theListener : theRegistered) {
            if (theListener instanceof JavaFXPropertyChangeListener) {
                aProperty.removeChangeListener(theListener);
            }
        }
    }
}
