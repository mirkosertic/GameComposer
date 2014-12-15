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
