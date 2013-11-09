package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectInstanceAddedToSceneEvent extends GameEvent {

    private GameObjectInstance instance;

    public GameObjectInstanceAddedToSceneEvent(GameObjectInstance aInstance) {
        instance = aInstance;
    }

    public GameObjectInstance getGameObjectInstance() {
        return instance;
    }
}
