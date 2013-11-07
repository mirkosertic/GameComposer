package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.GameObjectInstance;

public class DeleteGameObjectInstanceEvent {

    private GameObjectInstance gameObjectInstance;

    public DeleteGameObjectInstanceEvent(GameObjectInstance aInstance) {
        gameObjectInstance = aInstance;
    }

    public GameObjectInstance getGameObjectInstance() {
        return gameObjectInstance;
    }
}