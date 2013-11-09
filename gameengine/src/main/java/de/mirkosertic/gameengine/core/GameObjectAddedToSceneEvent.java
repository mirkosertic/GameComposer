package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectAddedToSceneEvent extends GameEvent {

    private GameObject object;

    public GameObjectAddedToSceneEvent(GameObject aObject) {
        object = aObject;
    }

    public GameObject getGameObject() {
        return object;
    }
}
