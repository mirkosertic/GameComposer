package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameLoopThrottle;

public class ThreadGameLoopThrottle implements GameLoopThrottle {

    @Override
    public void perhapsThrottleDown(long aDurationOfGameLoop) {
        // We just do nothing here
    }
}
