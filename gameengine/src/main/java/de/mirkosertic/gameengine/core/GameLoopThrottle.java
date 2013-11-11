package de.mirkosertic.gameengine.core;

public interface GameLoopThrottle {

    void perhapsThrottleDown(long aDurationOfGameLoop);
}
