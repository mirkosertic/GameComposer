package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.core.GameSystem;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.process.StartProcess;

public class GameSoundManager implements GameSystem {

    private final GameEventManager eventManager;
    private final GameSoundSystem soundSystem;

    GameSoundManager(GameSoundSystem aGameSoundSystem, GameEventManager aEventManager) {
        soundSystem = aGameSoundSystem;
        eventManager = aEventManager;
    }

    @Override
    public void proceedGame(long aTotalTicks, long aGameTime, long aElapsedTime) {
    }

    void onPlaySoundEvent(ResourceName aResourceName) {
        eventManager.fire(new StartProcess(new PlaySoundProcess(soundSystem, aResourceName)));
    }
}