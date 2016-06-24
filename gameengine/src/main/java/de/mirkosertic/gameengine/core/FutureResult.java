package de.mirkosertic.gameengine.core;

public interface FutureResult {

    void waitForCompletion() throws Exception;
}
