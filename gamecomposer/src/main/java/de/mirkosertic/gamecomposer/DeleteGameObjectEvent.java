package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.GameObject;

public class DeleteGameObjectEvent {

    private GameObject gameObject;

    public DeleteGameObjectEvent(GameObject aInstance) {
        gameObject = aInstance;
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}