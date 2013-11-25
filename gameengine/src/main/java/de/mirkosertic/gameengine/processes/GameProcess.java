package de.mirkosertic.gameengine.processes;

public interface GameProcess {

    static enum ProceedResult {
        STOPPED, CONTINUE_RUNNING
    }

    void started();

    ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop);

    void killed();

    GameProcess getChildProcess();
}
