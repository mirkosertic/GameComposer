package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectRemovedFromSceneEvent extends GameEvent {

    private GameObject object;

    public GameObjectRemovedFromSceneEvent(GameObject aObject) {
        object = aObject;
    }

    public GameObject getGameObject() {
        return object;
    }
}
