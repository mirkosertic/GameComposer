package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;

public class EnableDynamicPhysicsEvent extends GameEvent {

    private GameObjectInstance object;

    public EnableDynamicPhysicsEvent(GameObjectInstance aObject) {
        object = aObject;
    }

    public GameObjectInstance getObject() {
        return object;
    }
}
