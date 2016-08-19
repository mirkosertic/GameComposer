/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
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
        theNode.focusedProperty().addListener((observableValue, aOldValue, aNewValue) -> {
            if (aOldValue && !aNewValue) {
                aBeanProperty.set(aFXProperty.getValue());
            }
        });
        aFXProperty.setValue(aBeanProperty.get());
    }

    public static <T, V> void bindUIToBean(final Property<T> aBeanProperty, final javafx.beans.property.Property<V> aFXProperty, final Converter<T, V> aConverter) {
        Node theNode = (Node) aFXProperty.getBean();
        theNode.focusedProperty().addListener((observableValue, aOldValue, aNewValue) -> {
            if (aOldValue && !aNewValue) {
                aBeanProperty.set(aConverter.uiToBean(aFXProperty.getValue()));
            }
        });
        aFXProperty.setValue(aConverter.beanToUI(aBeanProperty.get()));
    }

    public static <T> void bind(final Property<T> aBeanProperty, final javafx.beans.property.Property<T> aFXProperty) {
        aBeanProperty.addChangeListener(aEvent -> aFXProperty.setValue((T) aEvent.property.get()));
        Node theNode = (Node) aFXProperty.getBean();
        theNode.focusedProperty().addListener((observableValue, aOldValue, aNewValue) -> {
            if (aOldValue && !aNewValue) {
                aBeanProperty.set(aFXProperty.getValue());
            }
        });
        aFXProperty.setValue(aBeanProperty.get());
    }

    public static <T, V> void bind(final Property<T> aBeanProperty, final javafx.beans.property.Property<V> aFXProperty, final Converter<T, V> aConverter) {
        aBeanProperty.addChangeListener(aEvent -> aFXProperty.setValue(aConverter.beanToUI((T) aEvent.property.get())));
        Node theNode = (Node) aFXProperty.getBean();
        theNode.focusedProperty().addListener((observableValue, aOldValue, aNewValue) -> {
            if (aOldValue && !aNewValue) {
                aBeanProperty.set(aConverter.uiToBean(aFXProperty.getValue()));
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
