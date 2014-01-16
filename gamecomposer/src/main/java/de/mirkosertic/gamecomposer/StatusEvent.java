package de.mirkosertic.gamecomposer;

public class StatusEvent {

    public static enum Severity {INFO, ERROR};

    private final String message;
    private final Severity severity;

    public StatusEvent(String aMessage, Severity aSeverity) {
        message = aMessage;
        severity = aSeverity;
    }

    public String getMessage() {
        return message;
    }

    public Severity getSeverity() {
        return severity;
    }
}
