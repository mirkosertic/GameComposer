package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.core.GameEvent;

public class StartProcessEvent extends GameEvent {

    private GameProcess process;

    public StartProcessEvent(GameProcess aProcess) {
        process = aProcess;
    }

    public GameProcess getProcess() {
        return process;
    }
}
