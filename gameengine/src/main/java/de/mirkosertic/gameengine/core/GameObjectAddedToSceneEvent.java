package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectAddedToSceneEvent extends GameEvent {

    private ReadOnlyProperty<GameObject> object;

    public GameObjectAddedToSceneEvent(GameObject aObject) {
        super("GameObjectAddedToSceneEvent");
        object = registerProperty(new ReadOnlyProperty<GameObject>(this, "object", aObject));
    }

    public ReadOnlyProperty<GameObject> objectProperty() {
        return object;
    }
}