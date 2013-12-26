package de.mirkosertic.gameengine.android;

import de.mirkosertic.gameengine.core.GameResourceCache;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.types.ResourceName;

public class AndroidGameSoundSystem implements GameSoundSystem {

    private final GameResourceCache resourceCache;

    AndroidGameSoundSystem(GameResourceCache aResourceCache) {
        resourceCache = aResourceCache;
    }

    @Override
    public Object play(ResourceName aResourceName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stop(Object aSoundObject) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
