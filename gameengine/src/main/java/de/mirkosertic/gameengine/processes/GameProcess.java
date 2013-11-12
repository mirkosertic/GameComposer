package de.mirkosertic.gameengine.processes;

public interface GameProcess {

    void started(GameProcessManager aProcessManager);

    void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop);

    void killed();
}
