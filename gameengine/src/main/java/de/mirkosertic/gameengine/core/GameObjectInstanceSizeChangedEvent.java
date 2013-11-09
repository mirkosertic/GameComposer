package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.types.Size;

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
