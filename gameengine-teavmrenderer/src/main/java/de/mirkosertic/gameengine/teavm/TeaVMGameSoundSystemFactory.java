package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;

public class TeaVMGameSoundSystemFactory implements GameSoundSystemFactory<TeaVMGameSoundSystem> {

    @Override
    public TeaVMGameSoundSystem create(GameRuntime aGameRuntime) {
        return new TeaVMGameSoundSystem(aGameRuntime);
    }
}
