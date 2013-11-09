package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectInstanceRemovedFromSceneEvent extends GameEvent {

    private GameObjectInstance instance;

    public GameObjectInstanceRemovedFromSceneEvent(GameObjectInstance aInstance) {
        instance = aInstance;
    }

    public GameObjectInstance getGameObjectInstance() {
        return instance;
    }
}
