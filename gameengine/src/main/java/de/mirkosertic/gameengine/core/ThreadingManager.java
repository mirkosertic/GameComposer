package de.mirkosertic.gameengine.core;

public interface ThreadingManager {

    FutureResult submit(Job aJob);
}
