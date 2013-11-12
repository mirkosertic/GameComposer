package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;

public class GameSoundManagerFactory {

    public static GameSoundManager create(GameEventManager aEventManager, GameSoundSystem aSoundSystem) {
        final GameSoundManager theManager = new GameSoundManager(aSoundSystem, aEventManager);
        aEventManager.register(null, PlaySoundEvent.class, new GameEventListener<PlaySoundEvent>() {
            @Override
            public void handleGameEvent(PlaySoundEvent aEvent) {
                theManager.onPlaySoundEvent(aEvent.resourceNameProperty().get());
            }
        });
        return theManager;
    }
}
