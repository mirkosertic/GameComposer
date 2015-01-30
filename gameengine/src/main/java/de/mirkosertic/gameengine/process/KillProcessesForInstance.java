package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;

@InheritedClassInformation
class KillProcessesForInstance extends GameEvent {

    private static final KillProcessesForInstanceClassInformation CIINSTANCE = new KillProcessesForInstanceClassInformation();

    @ReflectiveField
    public final GameObjectInstance instance;

    KillProcessesForInstance(GameObjectInstance aInstance) {
        super("KillProcessesForInstance");
        instance = aInstance;
    }

    @Override
    public KillProcessesForInstanceClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}