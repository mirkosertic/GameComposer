package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;

class KillProcessesForInstance extends GameEvent {

    public final GameObjectInstance instance;

    KillProcessesForInstance(GameObjectInstance aInstance) {
        super("KillProcessesForInstance");
        instance = aInstance;
    }
}
