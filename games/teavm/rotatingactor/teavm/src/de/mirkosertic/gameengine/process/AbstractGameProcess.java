package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.core.GameObjectInstance;

public abstract class AbstractGameProcess implements GameProcess {

    private GameProcess childProcess;

    @Override
    public void started() {
    }

    @Override
    public ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        return ProceedResult.STOPPED;
    }

    @Override
    public void killed() {
    }

    @Override
    public GameProcess getChildProcess() {
        return childProcess;
    }

    public void setChildProcess(GameProcess childProcess) {
        this.childProcess = childProcess;
    }

    @Override
    public boolean affectsInstance(GameObjectInstance aInstance) {
        return false;
    }
}