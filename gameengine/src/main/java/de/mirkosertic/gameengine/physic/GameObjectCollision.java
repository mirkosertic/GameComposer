package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;

@InheritedClassInformation
public class GameObjectCollision extends GameEvent {

    private static final GameObjectCollisionClassInformation CIINSTANCE = new GameObjectCollisionClassInformation();

    @ReflectiveField
    public final GameObjectInstance instance1;

    @ReflectiveField
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

    @Override
    public GameObjectCollisionClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
