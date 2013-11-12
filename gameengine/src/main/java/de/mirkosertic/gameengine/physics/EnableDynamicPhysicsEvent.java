package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class EnableDynamicPhysicsEvent extends GameEvent {

    private ReadOnlyProperty<GameObjectInstance> object;

    public EnableDynamicPhysicsEvent(GameObjectInstance aObject) {
        super("EnableDynamicPhysicsEvent");
        object = registerProperty(new ReadOnlyProperty<GameObjectInstance>(this, "object", aObject));
    }

    public ReadOnlyProperty<GameObjectInstance> objectProperty() {
        return object;
    }
}