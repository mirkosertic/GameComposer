package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.process.AbstractGameProcess;
import de.mirkosertic.gameengine.type.ResourceName;

//TODO: Add notifier to platform specific sound system to remove processes for already played sounds
public class PlaySoundProcess extends AbstractGameProcess {

    private final GameSoundSystem gameSoundSystem;
    private final ResourceName resourceName;
    private Object soundObject;

    PlaySoundProcess(GameSoundSystem aSoundSystem, ResourceName aResourceName) {
        gameSoundSystem = aSoundSystem;
        resourceName = aResourceName;
    }

    @Override
    public void started() {
        super.started();
        playSound();
    }

    private void playSound() {
        soundObject = gameSoundSystem.play(resourceName);
    }

    @Override
    public void killed() {
        if (soundObject != null) {
            gameSoundSystem.stop(soundObject);
        }
    }
}
