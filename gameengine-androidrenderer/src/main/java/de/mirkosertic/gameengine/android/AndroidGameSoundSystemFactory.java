package de.mirkosertic.gameengine.android;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;

public class AndroidGameSoundSystemFactory implements GameSoundSystemFactory {

    @Override
    public GameSoundSystem create(GameRuntime aGameRuntime) {
        return new AndroidGameSoundSystem(aGameRuntime.getResourceCache());
    }
}
