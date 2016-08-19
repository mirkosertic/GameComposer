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
package de.mirkosertic.gamecomposer.objectinspector;

import de.mirkosertic.gameengine.event.Property;

import java.util.Optional;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class PropertyEditorItem<T> implements PropertySheet.Item {

    private final Property<T> property;
    private final String category;
    private final String name;
    private final String description;
    private final Optional<Class<? extends PropertyEditor<?>>> propertyEditor;

    public PropertyEditorItem(String aCategory, Property<T> aProperty, String aName, String aDescription, Optional<Class<? extends PropertyEditor<?>>> aPropertyEditor) {
        property = aProperty;
        category = aCategory;
        name = aName;
        description = aDescription;
        propertyEditor = aPropertyEditor;
    }

    public PropertyEditorItem(String aCategory, Property<T> aProperty, String aName, String aDescription) {
        this(aCategory, aProperty, aName, aDescription, Optional.empty());
    }

    @Override
    public Class<?> getType() {
        return property.getType();
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Object getValue() {
        return property.get();
    }

    @Override
    public void setValue(Object aValue) {
        property.set((T) aValue);
    }

    @Override
    public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
        return propertyEditor;
    }

    public Object getOwner() {
        return property.getOwner();
    }
}