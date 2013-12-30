package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

class KillProcessesForInstance extends GameEvent {

    private final Property<GameObjectInstance> instance;

    public KillProcessesForInstance(GameObjectInstance aInstance) {
        super("StartProcess");
        instance = registerProperty(new Property<GameObjectInstance>(this, "instance", aInstance));
    }

    public ReadOnlyProperty<GameObjectInstance> instanceProperty() {
        return instance;
    }
}
