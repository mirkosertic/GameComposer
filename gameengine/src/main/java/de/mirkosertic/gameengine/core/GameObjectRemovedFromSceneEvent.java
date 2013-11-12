package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectRemovedFromSceneEvent extends GameEvent {

    private ReadOnlyProperty<GameObject> object;

    public GameObjectRemovedFromSceneEvent(GameObject aObject) {
        super("GameObjectRemovedFromSceneEvent");
        object = registerProperty(new ReadOnlyProperty<GameObject>(this, "object", aObject));
    }

    public ReadOnlyProperty<GameObject> objectProperty() {
        return object;
    }
}
