package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;

public class ApplyForceToGameObjectInstance extends GameEvent {

    private GameObjectInstance instance;
    private float forceX;
    private float forceY;

    public ApplyForceToGameObjectInstance(GameObjectInstance aInstance, float aForceX, float aForceY) {
        instance = aInstance;
        forceX = aForceX;
        forceY = aForceY;
    }

    public GameObjectInstance getGameObjectInstance() {
        return instance;
    }

    public float getForceX() {
        return forceX;
    }

    public float getForceY() {
        return forceY;
    }
}
