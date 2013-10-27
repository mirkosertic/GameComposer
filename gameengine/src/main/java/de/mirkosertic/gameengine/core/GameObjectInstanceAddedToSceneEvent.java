package de.mirkosertic.gameengine.core;

public class GameObjectInstanceAddedToSceneEvent extends GameEvent {

    private GameObjectInstance instance;

    public GameObjectInstanceAddedToSceneEvent(GameObjectInstance aInstance) {
        instance = aInstance;
    }

    public GameObjectInstance getGameObjectInstance() {
        return instance;
    }
}
