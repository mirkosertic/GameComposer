package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;

public class DragomeGameSoundSystemFactory implements GameSoundSystemFactory {

    @Override
    public GameSoundSystem create(GameRuntime aGameRuntime) {
        return new DragomeGameSoundSystem(aGameRuntime);
    }
}