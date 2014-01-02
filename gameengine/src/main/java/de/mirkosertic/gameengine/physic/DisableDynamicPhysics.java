package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;

public class DisableDynamicPhysics extends GameEvent {

    public final GameObjectInstance object;

    public DisableDynamicPhysics(GameObjectInstance aObject) {
        super("DisableDynamicPhysics");
        object = aObject;
    }
}
