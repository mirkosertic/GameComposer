package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;

import com.google.gwt.media.client.Audio;

public class GWTGameSoundSystemFactory implements GameSoundSystemFactory<Audio> {

    @Override
    public GameSoundSystem<Audio> create(GameRuntime aGameRuntime) {
        return new GWTGameSoundSystem(aGameRuntime.getResourceCache());
    }
}
