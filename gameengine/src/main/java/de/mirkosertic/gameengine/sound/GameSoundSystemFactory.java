package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.core.GameRuntime;

public interface GameSoundSystemFactory<T> {

    GameSoundSystem<T> create(GameRuntime aGameRuntime);
}
