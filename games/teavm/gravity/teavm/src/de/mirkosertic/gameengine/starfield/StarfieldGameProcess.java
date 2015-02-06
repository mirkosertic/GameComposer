package de.mirkosertic.gameengine.starfield;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.process.GameProcess;

public class StarfieldGameProcess implements GameProcess {

    private final StarfieldGameSceneEffect effect;

    public StarfieldGameProcess(StarfieldGameSceneEffect effect) {
        this.effect = effect;
    }

    @Override
    public void started() {
    }

    @Override
    public boolean affectsInstance(GameObjectInstance aInstance) {
        return false;
    }

    @Override
    public ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        return effect.proceedGame(aGameTime, aElapsedTimeSinceLastLoop);
    }

    @Override
    public void killed() {
    }

    @Override
    public GameProcess getChildProcess() {
        return null;
    }
}
