package de.mirkosertic.gameengine.core;

public class GameObjectInstanceSizeChangedEvent extends GameEvent {

    private GameObjectInstance instance;
    private Size size;

    public GameObjectInstanceSizeChangedEvent(GameObjectInstance aInstance, Size aSize) {
        instance = aInstance;
        size = aSize;
    }

    public GameObjectInstance getGameObjectInstance() {
        return instance;
    }

    public Size getSize() {
        return size;
    }
}
