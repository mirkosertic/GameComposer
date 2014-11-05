package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

public class DragomeGameSoundSystem implements GameSoundSystem<DragomeSound> {

    private final GameRuntime gameRuntime;

    public DragomeGameSoundSystem(GameRuntime aRuntime) {
        gameRuntime = aRuntime;
    }

    @Override
    public DragomeSound play(ResourceName aResourceName) {
        return new DragomeSound();
    }

    @Override
    public void stop(DragomeSound aSoundObject) {
    }
}
