package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;

public class GameSoundManagerFactory {

    public static GameSoundManager create(GameEventManager aEventManager, GameSoundSystem aSoundSystem) {
        final GameSoundManager theManager = new GameSoundManager(aSoundSystem, aEventManager);
        aEventManager.register(null, PlaySound.class, new GameEventListener<PlaySound>() {
            @Override
            public void handleGameEvent(PlaySound aEvent) {
                theManager.onPlaySoundEvent(aEvent.resourceName);
            }
        });
        return theManager;
    }
}
