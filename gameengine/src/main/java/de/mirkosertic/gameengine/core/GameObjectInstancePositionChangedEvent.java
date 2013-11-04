package de.mirkosertic.gameengine.core;

public class GameObjectInstancePositionChangedEvent extends GameEvent {

    private GameObjectInstance instance;
    private Position position;

    public GameObjectInstancePositionChangedEvent(GameObjectInstance aInstance, Position aPosition) {
        instance = aInstance;
        position = aPosition;
    }

    public GameObjectInstance getGameObjectInstance() {
        return instance;
    }

    public Position getPosition() {
        return position;
    }
}
