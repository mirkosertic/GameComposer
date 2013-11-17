package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class ApplyForceToGameObjectInstance extends GameEvent {

    private final Property<GameObjectInstance> instance;
    private final Property<Float> forceX;
    private final Property<Float> forceY;

    public ApplyForceToGameObjectInstance(GameObjectInstance aInstance, float aForceX, float aForceY) {
        super("ApplyForceToGameObjectInstance");
        instance = registerProperty(new Property<GameObjectInstance>(this, "instance", aInstance));
        forceX = registerProperty(new Property<Float>(this, "forceX", aForceX));
        forceY = registerProperty(new Property<Float>(this, "forceY", aForceY));
    }

    public ReadOnlyProperty<GameObjectInstance> instanceProperty() {
        return instance;
    }

    public ReadOnlyProperty<Float> forceXProperty() {
        return forceX;
    }

    public ReadOnlyProperty<Float> forceYProperty() {
        return forceY;
    }
}
