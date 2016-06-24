package de.mirkosertic.gameengine.core;

public interface GameSystem {

    GameSystemWork proceedGame(long aTotalTicks, long aGameTime, long aElapsedTime);
}