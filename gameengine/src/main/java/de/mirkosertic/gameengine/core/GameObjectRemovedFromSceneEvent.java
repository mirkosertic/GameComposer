package de.mirkosertic.gameengine.core;

public class GameObjectRemovedFromSceneEvent extends GameEvent {

    private GameObject object;

    public GameObjectRemovedFromSceneEvent(GameObject aObject) {
        object = aObject;
    }

    public GameObject getGameObjectInstance() {
        return object;
    }
}
