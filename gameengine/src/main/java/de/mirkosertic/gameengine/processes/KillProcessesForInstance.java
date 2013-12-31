package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;

class KillProcessesForInstance extends GameEvent {

    public final GameObjectInstance instance;

    public KillProcessesForInstance(GameObjectInstance aInstance) {
        super("StartProcess");
        instance = aInstance;
    }
}
