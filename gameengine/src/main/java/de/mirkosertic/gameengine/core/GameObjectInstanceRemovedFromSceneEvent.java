package de.mirkosertic.gameengine.core;

public class GameObjectInstanceRemovedFromSceneEvent extends GameEvent {

    private GameObjectInstance instance;

    public GameObjectInstanceRemovedFromSceneEvent(GameObjectInstance aInstance) {
        instance = aInstance;
    }

    public GameObjectInstance getGameObjectInstance() {
        return instance;
    }
}
