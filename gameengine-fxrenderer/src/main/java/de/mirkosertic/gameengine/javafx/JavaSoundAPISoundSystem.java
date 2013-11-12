package de.mirkosertic.gameengine.javafx;

import java.io.IOException;

import javax.sound.sampled.Clip;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceCache;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.types.ResourceName;
import de.mirkosertic.gameengine.sound.GameSoundSystem;

public class JavaSoundAPISoundSystem implements GameSoundSystem<Clip> {

    private GameResourceCache resourceCache;

    JavaSoundAPISoundSystem(GameResourceCache aResourceCache) {
        resourceCache = aResourceCache;
    }

    @Override
    public Clip play(ResourceName aResourceName) {
        try {
            GameResource theResource = resourceCache.getResourceFor(aResourceName);
            if (theResource.getType() == GameResourceType.SOUND) {
                Clip theClip = ((JavaFXAudioResource) theResource).getClip();
                theClip.start();
                return theClip;
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop(Clip aSoundObject) {
        if (aSoundObject.isRunning()) {
            aSoundObject.stop();
        }
    }
}