package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class ApplyForceToGameObjectInstance extends GameEvent {

    private final ReadOnlyProperty<GameObjectInstance> instance;
    private final ReadOnlyProperty<Float> forceX;
    private final ReadOnlyProperty<Float> forceY;

    public ApplyForceToGameObjectInstance(GameObjectInstance aInstance, float aForceX, float aForceY) {
        super("ApplyForceToGameObjectInstance");
        instance = registerProperty(new ReadOnlyProperty<GameObjectInstance>(this, "instance", aInstance));
        forceX = registerProperty(new ReadOnlyProperty<Float>(this, "forceX", aForceX));
        forceY = registerProperty(new ReadOnlyProperty<Float>(this, "forceY", aForceY));
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
