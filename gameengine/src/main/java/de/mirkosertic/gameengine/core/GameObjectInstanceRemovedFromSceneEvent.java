package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectInstanceRemovedFromSceneEvent extends GameEvent {

    private ReadOnlyProperty<GameObjectInstance> instance;

    public GameObjectInstanceRemovedFromSceneEvent(GameObjectInstance aInstance) {
        super("GameObjectInstanceRemovedFromSceneEvent");
        instance = registerProperty(new ReadOnlyProperty<GameObjectInstance>(this, "instance", aInstance));
    }

    public ReadOnlyProperty<GameObjectInstance> instanceProperty() {
        return instance;
    }
}
