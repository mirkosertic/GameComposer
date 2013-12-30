package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.core.GameObjectInstance;

public interface GameProcess {

    static enum ProceedResult {
        STOPPED, CONTINUE_RUNNING
    }

    void started();

    boolean affectsInstance(GameObjectInstance aInstance);

    ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop);

    void killed();

    GameProcess getChildProcess();
}
