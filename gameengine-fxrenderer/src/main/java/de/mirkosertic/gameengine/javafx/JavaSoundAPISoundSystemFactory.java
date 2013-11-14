package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;

import javax.sound.sampled.Clip;

public class JavaSoundAPISoundSystemFactory implements GameSoundSystemFactory<Clip> {

    @Override
    public GameSoundSystem<Clip> create(GameRuntime aGameRuntime) {
        return new JavaSoundAPISoundSystem(aGameRuntime.getResourceCache());
    }
}
