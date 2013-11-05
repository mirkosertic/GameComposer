package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;

public class DisableDynamicPhysicsEvent extends GameEvent {

    private GameObjectInstance object;

    public DisableDynamicPhysicsEvent(GameObjectInstance aObject) {
        object = aObject;
    }

    public GameObjectInstance getObject() {
        return object;
    }
}
