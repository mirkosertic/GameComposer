package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.FutureResult;
import de.mirkosertic.gameengine.core.Job;
import de.mirkosertic.gameengine.core.ThreadingManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JDKThreadingManager implements ThreadingManager {

    private final ExecutorService executorService;

    public JDKThreadingManager() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public FutureResult submit(final Job aJob) {
        final Future theFuture = executorService.submit((Runnable) () -> aJob.run());
        return () -> theFuture.get();
    }
}
