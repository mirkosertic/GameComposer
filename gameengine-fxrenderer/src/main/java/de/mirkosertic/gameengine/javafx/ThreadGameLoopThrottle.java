package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameLoopThrottle;

public class ThreadGameLoopThrottle implements GameLoopThrottle {

    @Override
    public void perhapsThrottleDown(long aDurationOfGameLoop) {
        if (aDurationOfGameLoop < 16) {
            try {
                Thread.sleep(16 - aDurationOfGameLoop);
            } catch (InterruptedException e) {
                // Do nothing here
            }
        }

    }
}
