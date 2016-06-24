package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.core.GameSystem;
import de.mirkosertic.gameengine.core.GameSystemWork;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.process.StartProcess;
import de.mirkosertic.gameengine.type.ResourceName;

public class GameSoundManager implements GameSystem {

    private final GameEventManager eventManager;
    private final GameSoundSystem soundSystem;

    GameSoundManager(GameSoundSystem aGameSoundSystem, GameEventManager aEventManager) {
        soundSystem = aGameSoundSystem;
        eventManager = aEventManager;
    }

    @Override
    public GameSystemWork proceedGame(long aTotalTicks, long aGameTime, long aElapsedTime) {
        return GameSystemWork.NOTHING;
    }

    void onPlaySoundEvent(ResourceName aResourceName) {
        eventManager.fire(new StartProcess(new PlaySoundProcess(soundSystem, aResourceName)));
    }
}