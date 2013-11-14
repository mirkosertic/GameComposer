package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectCollisionEvent extends GameEvent {

    private final ReadOnlyProperty<GameObjectInstance> instance1;
    private final ReadOnlyProperty<GameObjectInstance> instance2;

    public GameObjectCollisionEvent(GameObjectInstance aInstance1, GameObjectInstance aInstance2) {
        super("GameObjectCollisionEvent");
        instance1 = registerProperty(new ReadOnlyProperty<GameObjectInstance>(this, "instance1", aInstance1));
        instance2 = registerProperty(new ReadOnlyProperty<GameObjectInstance>(this, "instance", aInstance2));
    }

    public GameObjectInstance getOtherInstanceOrNullIfNotAffected(GameObjectInstance aOtherInstance) {
        if (instance1.get() == aOtherInstance) {
            return instance2.get();
        }
        if (instance2.get() == aOtherInstance) {
            return instance1.get();
        }
        return null;
    }

    public ReadOnlyProperty<GameObjectInstance> instance1Property() {
        return instance1;
    }

    public ReadOnlyProperty<GameObjectInstance> instance2Property() {
        return instance2;
    }
}
