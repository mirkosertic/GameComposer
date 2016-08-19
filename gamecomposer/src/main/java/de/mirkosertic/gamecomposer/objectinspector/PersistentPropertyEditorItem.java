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

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.event.Property;

import java.util.Optional;

public class PersistentPropertyEditorItem<T> extends PropertyEditorItem<T> {

    private final PersistenceManager persistenceManager;

    public PersistentPropertyEditorItem(PersistenceManager aPersistenceManager, String aCategory, Property<T> aProperty, String aName, String aDescription, Optional<Class<? extends org.controlsfx.property.editor.PropertyEditor<?>>> aPropertyEditor) {
        super(aCategory, aProperty, aName, aDescription, aPropertyEditor);
        persistenceManager = aPersistenceManager;
    }

    public PersistentPropertyEditorItem(PersistenceManager aPersistenceManager, String aCategory, Property<T> aProperty, String aName, String aDescription) {
        super(aCategory, aProperty, aName, aDescription);
        persistenceManager = aPersistenceManager;
    }

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }
}
