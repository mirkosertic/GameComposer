package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceCache;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

import javax.sound.sampled.Clip;

public class JavaSoundAPISoundSystem implements GameSoundSystem<Clip> {

    private final GameResourceCache resourceCache;

    JavaSoundAPISoundSystem(GameResourceCache aResourceCache) {
        resourceCache = aResourceCache;
    }

    @Override
    public Clip play(ResourceName aResourceName) {
        try {
            GameResource theResource = resourceCache.getResourceFor(aResourceName);
            if (theResource.getType() == GameResourceType.SOUND) {
                Clip theClip = ((JavaFXAudioResource) theResource).createClip();
                theClip.start();
                return theClip;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
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