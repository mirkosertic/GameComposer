package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectInstanceAddedToSceneEvent extends GameEvent {

    private ReadOnlyProperty<GameObjectInstance> instance;

    public GameObjectInstanceAddedToSceneEvent(GameObjectInstance aInstance) {
        super("GameObjectInstanceAddedToSceneEvent");
        instance = registerProperty(new ReadOnlyProperty<GameObjectInstance>(this, "instance", aInstance));
    }

    public ReadOnlyProperty<GameObjectInstance> instanceProperty() {
        return instance;
    }
}