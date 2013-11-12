package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.core.ResourceName;

public interface GameSoundSystem<T> {

    T play(ResourceName aResourceName);

    void stop(T aSoundObject);
}
