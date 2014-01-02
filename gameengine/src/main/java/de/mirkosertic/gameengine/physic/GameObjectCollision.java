package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectCollision extends GameEvent {

    public final GameObjectInstance instance1;
    public final GameObjectInstance instance2;

    public GameObjectCollision(GameObjectInstance aInstance1, GameObjectInstance aInstance2) {
        super("GameObjectCollision");
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
