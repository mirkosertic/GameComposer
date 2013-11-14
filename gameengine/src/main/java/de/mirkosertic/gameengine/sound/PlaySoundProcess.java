package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.types.ResourceName;
import de.mirkosertic.gameengine.processes.AbstractGameProcess;
import de.mirkosertic.gameengine.processes.GameProcessManager;

public class PlaySoundProcess extends AbstractGameProcess {

    private final GameSoundSystem gameSoundSystem;
    private final ResourceName resourceName;
    private Object soundObject;

    PlaySoundProcess(GameSoundSystem aSoundSystem, ResourceName aResourceName) {
        gameSoundSystem = aSoundSystem;
        resourceName = aResourceName;
    }

    @Override
    public void started(GameProcessManager aProcessManager) {
        super.started(aProcessManager);
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
