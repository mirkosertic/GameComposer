package de.mirkosertic.gameengine.processes;

public interface GameProcess {

    void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop);
}
