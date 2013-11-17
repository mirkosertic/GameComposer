package de.mirkosertic.gameengine.event;

public class GameEvent extends PropertyAware {

    private final ReadOnlyProperty<String> type;

    protected GameEvent(String aType) {
        type = new ReadOnlyProperty<String>(this, "type", aType);
    }

    public ReadOnlyProperty<String> typeProperty() {
        return type;
    }
}