package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class ApplyForceToGameObjectInstance extends GameEvent {

    private ReadOnlyProperty<GameObjectInstance> instance;
    private ReadOnlyProperty<Float> forceX;
    private ReadOnlyProperty<Float> forceY;

    public ApplyForceToGameObjectInstance(GameObjectInstance aInstance, float aForceX, float aForceY) {
        super("ApplyForceToGameObjectInstance");
        instance = registerProperty(new ReadOnlyProperty<GameObjectInstance>(this, "instance", aInstance));
        forceX = registerProperty(new ReadOnlyProperty<Float>(this, "forceX", Float.valueOf(aForceX)));
        forceY = registerProperty(new ReadOnlyProperty<Float>(this, "forceY", Float.valueOf(aForceY)));
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
