package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;

public class SystemException extends GameEvent {

    public final Exception exception;

    public SystemException(Exception aException) {
        super("SystemException");
        exception = aException;
    }

    @Override
    public String toString() {
        return exception.getMessage();
    }
}
