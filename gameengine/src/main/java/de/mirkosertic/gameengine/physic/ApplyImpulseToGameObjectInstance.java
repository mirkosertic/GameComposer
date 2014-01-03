package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.Force;

public class ApplyImpulseToGameObjectInstance extends GameEvent {

    public final GameObjectInstance instance;
    public final Force force;

    public ApplyImpulseToGameObjectInstance(GameObjectInstance aInstance, Force aForce) {
        super("ApplyImpulseToGameObjectInstance");
        instance = aInstance;
        force = aForce;
    }
}