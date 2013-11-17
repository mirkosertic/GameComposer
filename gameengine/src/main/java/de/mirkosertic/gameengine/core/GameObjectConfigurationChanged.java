package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectConfigurationChanged extends GameEvent {

    private final Property<GameObject> object;

    public GameObjectConfigurationChanged(GameObject aObject) {
        super("GameObjectConfigurationChanged");
        object = registerProperty(new Property<GameObject>(this, "object", aObject));
    }

    public ReadOnlyProperty<GameObject> objectProperty() {
        return object;
    }
}