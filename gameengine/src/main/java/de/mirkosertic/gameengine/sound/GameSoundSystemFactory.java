package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.core.GameResourceCache;

public interface GameSoundSystemFactory<T> {

    GameSoundSystem<T> create(GameResourceCache aResourceCache);
}
