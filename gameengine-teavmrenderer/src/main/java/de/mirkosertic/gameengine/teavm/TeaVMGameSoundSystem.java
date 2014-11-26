package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

public class TeaVMGameSoundSystem implements GameSoundSystem {

    private final GameRuntime runtime;

    public TeaVMGameSoundSystem(GameRuntime aRuntime) {
        runtime = aRuntime;
    }

    @Override
    public Object play(ResourceName aResourceName) {
        return null;
    }

    @Override
    public void stop(Object aSoundObject) {
    }
}
