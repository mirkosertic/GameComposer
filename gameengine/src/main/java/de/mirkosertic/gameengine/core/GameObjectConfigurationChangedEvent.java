package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectConfigurationChangedEvent extends GameEvent {

    private ReadOnlyProperty<GameObject> object;

    public GameObjectConfigurationChangedEvent(GameObject aObject) {
        super("GameObjectConfigurationChangedEvent");
        object = registerProperty(new ReadOnlyProperty<GameObject>(this, "object", aObject));
    }

    public ReadOnlyProperty<GameObject> objectProperty() {
        return object;
    }
}