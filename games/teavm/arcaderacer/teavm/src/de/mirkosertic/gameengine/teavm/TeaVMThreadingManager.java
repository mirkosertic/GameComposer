package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.FutureResult;
import de.mirkosertic.gameengine.core.Job;
import de.mirkosertic.gameengine.core.ThreadingManager;

public class TeaVMThreadingManager implements ThreadingManager {

    @Override
    public FutureResult submit(final Job aJob) {

        final Object LOCK = new Object();

        synchronized (LOCK) {
            final Thread theThread = new Thread() {
                @Override
                public void run() {
                    synchronized (LOCK) {
                        TeaVMLogger.time("Thread");
                        aJob.run();
                        TeaVMLogger.timeEnd("Thread");
                    }
                }
            };
            theThread.start();
        }

        return new FutureResult() {
            @Override
            public void waitForCompletion() throws Exception {
                synchronized (LOCK) {
                    return;
                }

            }
        };
    }
}