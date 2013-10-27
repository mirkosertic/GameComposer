package de.mirkosertic.gameengine.core;

public interface GameSystem {

    void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop);
}
