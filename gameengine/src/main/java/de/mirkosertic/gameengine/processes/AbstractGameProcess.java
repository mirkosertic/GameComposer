package de.mirkosertic.gameengine.processes;

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

    public GameProcess getChildProcess() {
        return childProcess;
    }

    public void setChildProcess(GameProcess childProcess) {
        this.childProcess = childProcess;
    }
}