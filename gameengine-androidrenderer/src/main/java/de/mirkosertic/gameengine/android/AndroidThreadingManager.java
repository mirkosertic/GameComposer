package de.mirkosertic.gameengine.android;

import de.mirkosertic.gameengine.core.FutureResult;
import de.mirkosertic.gameengine.core.Job;
import de.mirkosertic.gameengine.core.ThreadingManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AndroidThreadingManager implements ThreadingManager {

    private final ExecutorService executorService;

    public AndroidThreadingManager() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public FutureResult submit(final Job aJob) {
        final Future theFuture = executorService.submit(new Runnable() {
            @Override
            public void run() {
                aJob.run();
            }
        });
        return new FutureResult() {
            @Override
            public void waitForCompletion() throws Exception {
                theFuture.get();
            }
        };
    }
}
