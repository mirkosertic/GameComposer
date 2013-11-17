package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.core.GameSystem;
import de.mirkosertic.gameengine.types.ResourceName;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.processes.StartProcess;

public class GameSoundManager implements GameSystem {

    private final GameEventManager eventManager;
    private final GameSoundSystem soundSystem;

    GameSoundManager(GameSoundSystem aGameSoundSystem, GameEventManager aEventManager) {
        soundSystem = aGameSoundSystem;
        eventManager = aEventManager;
    }

    void onPlaySoundEvent(ResourceName aResourceName) {
        eventManager.fire(new StartProcess(new PlaySoundProcess(soundSystem, aResourceName)));
    }

    @Override
    public void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
    }
}