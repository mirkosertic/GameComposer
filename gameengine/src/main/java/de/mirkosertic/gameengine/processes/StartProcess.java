package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.event.GameEvent;

public class StartProcess extends GameEvent {

    public final GameProcess process;

    public StartProcess(GameProcess aProcess) {
        super("StartProcess");
        process = aProcess;
    }
}
