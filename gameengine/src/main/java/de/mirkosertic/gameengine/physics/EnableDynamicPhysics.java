package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;

public class EnableDynamicPhysics extends GameEvent {

    public final GameObjectInstance object;

    public EnableDynamicPhysics(GameObjectInstance aObject) {
        super("EnableDynamicPhysics");
        object = aObject;
    }
}