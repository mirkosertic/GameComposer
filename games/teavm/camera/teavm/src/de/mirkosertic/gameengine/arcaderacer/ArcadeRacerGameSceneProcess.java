package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.process.GameProcess;

public class ArcadeRacerGameSceneProcess implements GameProcess {

    private final ArcadeRacerGameSceneEffect effect;

    public ArcadeRacerGameSceneProcess(ArcadeRacerGameSceneEffect aEffect) {
        effect = aEffect;
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
        return effect.proceesGame(aGameTime, aElapsedTimeSinceLastLoop);
    }

    @Override
    public void killed() {

    }

    @Override
    public GameProcess getChildProcess() {
        return null;
    }
}
