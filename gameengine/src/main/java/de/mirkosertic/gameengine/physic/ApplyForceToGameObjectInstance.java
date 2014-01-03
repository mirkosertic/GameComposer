package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.Force;

public class ApplyForceToGameObjectInstance extends GameEvent {

    public final GameObjectInstance instance;
    public final Force force;

    public ApplyForceToGameObjectInstance(GameObjectInstance aInstance, Force aForce) {
        super("ApplyForceToGameObjectInstance");
        instance = aInstance;
        force = aForce;
    }
}
