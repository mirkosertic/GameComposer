package de.mirkosertic.gameengine.core;

public interface GameSystem {
    void proceedGame(long aTotalTicks, long aGameTime, long aElapsedTime);
}