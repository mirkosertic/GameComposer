package de.mirkosertic.gameengine.core;

public class NoThreadingThreadingManager implements ThreadingManager {

    @Override
    public FutureResult submit(final Job aJob) {
        aJob.run();
        return new FutureResult() {
            @Override
            public void waitForCompletion() {
            }
        };
    }
}
