package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class ApplyImpulseToGameObjectInstance extends GameEvent {

    private final Property<GameObjectInstance> instance;
    private final Property<Float> impulseX;
    private final Property<Float> impulseY;

    public ApplyImpulseToGameObjectInstance(GameObjectInstance aInstance, float aImpulseX, float aImpulseY) {
        super("ApplyImpulseToGameObjectInstance");
        instance = registerProperty(new Property<GameObjectInstance>(this, "instance", aInstance));
        impulseX = registerProperty(new Property<Float>(this, "impulseX", aImpulseX));
        impulseY = registerProperty(new Property<Float>(this, "impulseY", aImpulseY));
    }

    public ReadOnlyProperty<GameObjectInstance> instanceProperty() {
        return instance;
    }

    public ReadOnlyProperty<Float> impulseXProperty() {
        return impulseX;
    }

    public ReadOnlyProperty<Float> impulseYProperty() {
        return impulseY;
    }
}
