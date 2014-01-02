package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;

public class ApplyImpulseToGameObjectInstance extends GameEvent {

    public final GameObjectInstance instance;
    public final float impulseX;
    public final float impulseY;

    public ApplyImpulseToGameObjectInstance(GameObjectInstance aInstance, float aImpulseX, float aImpulseY) {
        super("ApplyImpulseToGameObjectInstance");
        instance = aInstance;
        impulseX = aImpulseX;
        impulseY = aImpulseY;
    }
}