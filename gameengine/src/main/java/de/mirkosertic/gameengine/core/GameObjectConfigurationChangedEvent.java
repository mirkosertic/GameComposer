package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectConfigurationChangedEvent extends GameEvent {

    private GameObject object;

    public GameObjectConfigurationChangedEvent(GameObject aObject) {
        object = aObject;
    }

    public GameObject getGameObject() {
        return object;
    }
}
