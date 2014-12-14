package de.mirkosertic.gamecomposer.objectinspector;

import de.mirkosertic.gamecomposer.PersistenceManager;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class EditorContext {

    @Inject
    PersistenceManager persistenceManager;

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }
}