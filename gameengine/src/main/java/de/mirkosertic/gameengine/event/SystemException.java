package de.mirkosertic.gameengine.event;

public class SystemException extends GameEvent {

    public final Exception exception;

    public SystemException(Exception aException) {
        super("SystemException");
        exception = aException;
    }
}
