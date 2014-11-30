package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;

public class TeaVMGameSoundSystemFactory implements GameSoundSystemFactory<TeaVMGameSoundSystem> {

    @Override
    public GameSoundSystem create(GameRuntime aGameRuntime) {
        return new TeaVMGameSoundSystem(aGameRuntime);
    }
}
