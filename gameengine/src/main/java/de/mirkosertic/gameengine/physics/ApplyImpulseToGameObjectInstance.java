package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class ApplyImpulseToGameObjectInstance extends GameEvent {

    private final ReadOnlyProperty<GameObjectInstance> instance;
    private final ReadOnlyProperty<Float> impulseX;
    private final ReadOnlyProperty<Float> impulseY;

    public ApplyImpulseToGameObjectInstance(GameObjectInstance aInstance, float aImpulseX, float aImpulseY) {
        super("ApplyImpulseToGameObjectInstance");
        instance = registerProperty(new ReadOnlyProperty<GameObjectInstance>(this, "instance", aInstance));
        impulseX = registerProperty(new ReadOnlyProperty<Float>(this, "impulseX", aImpulseX));
        impulseY = registerProperty(new ReadOnlyProperty<Float>(this, "impulseY", aImpulseY));
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
