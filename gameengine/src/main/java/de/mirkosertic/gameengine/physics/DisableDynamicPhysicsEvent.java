package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class DisableDynamicPhysicsEvent extends GameEvent {

    private final ReadOnlyProperty<GameObjectInstance> object;

    public DisableDynamicPhysicsEvent(GameObjectInstance aObject) {
        super("DisableDynamicPhysicsEvent");
        object = registerProperty(new ReadOnlyProperty<GameObjectInstance>(this, "object", aObject));
    }

    public ReadOnlyProperty<GameObjectInstance> objectProperty() {
        return object;
    }
}
