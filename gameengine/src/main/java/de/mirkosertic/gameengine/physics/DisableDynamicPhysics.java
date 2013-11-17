package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class DisableDynamicPhysics extends GameEvent {

    private final Property<GameObjectInstance> object;

    public DisableDynamicPhysics(GameObjectInstance aObject) {
        super("DisableDynamicPhysics");
        object = registerProperty(new Property<GameObjectInstance>(this, "object", aObject));
    }

    public ReadOnlyProperty<GameObjectInstance> objectProperty() {
        return object;
    }
}
