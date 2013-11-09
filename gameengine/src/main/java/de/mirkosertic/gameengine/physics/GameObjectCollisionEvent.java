package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;

public class GameObjectCollisionEvent extends GameEvent {

    private GameObjectInstance instance1;
    private GameObjectInstance instance2;

    public GameObjectCollisionEvent(GameObjectInstance aInstance1, GameObjectInstance aInstance2) {
        instance1 = aInstance1;
        instance2 = aInstance2;
    }

    public GameObjectInstance getOtherInstanceOrNullIfNotAffected(GameObjectInstance aOtherInstance) {
        if (instance1 == aOtherInstance) {
            return instance2;
        }
        if (instance2 == aOtherInstance) {
            return instance1;
        }
        return null;
    }
}
