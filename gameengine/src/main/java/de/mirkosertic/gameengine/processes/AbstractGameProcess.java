package de.mirkosertic.gameengine.processes;

public abstract class AbstractGameProcess implements GameProcess {

    private GameProcessManager processManager;

    @Override
    public void started(GameProcessManager aProcessManager) {
        processManager = aProcessManager;
    }

    protected void kill() {
        if (processManager != null) {
            processManager.kill(this);
        }
    }

    @Override
    public void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
    }

    @Override
    public void killed() {
    }
}