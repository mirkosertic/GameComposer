package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;

public class ApplyForceToGameObjectInstance extends GameEvent {

    public final GameObjectInstance instance;
    public final float forceX;
    public final float forceY;

    public ApplyForceToGameObjectInstance(GameObjectInstance aInstance, float aForceX, float aForceY) {
        super("ApplyForceToGameObjectInstance");
        instance = aInstance;
        forceX = aForceX;
        forceY = aForceY;
    }
}
