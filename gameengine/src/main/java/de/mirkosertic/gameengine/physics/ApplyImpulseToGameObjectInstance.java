package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;

public class ApplyImpulseToGameObjectInstance extends GameEvent {

    private GameObjectInstance instance;
    private float impulseX;
    private float impulseY;

    public ApplyImpulseToGameObjectInstance(GameObjectInstance aInstance, float aImpulseX, float aImpulseY) {
        instance = aInstance;
        impulseX = aImpulseX;
        impulseY = aImpulseY;
    }

    public GameObjectInstance getGameObjectInstance() {
        return instance;
    }

    public float getImpulseX() {
        return impulseX;
    }

    public float getImpulseY() {
        return impulseY;
    }
}
