package de.mirkosertic.gameengine.android;

import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import de.mirkosertic.gameengine.core.GameResourceCache;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.types.ResourceName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AndroidGameSoundSystem implements GameSoundSystem<AndroidStreamIdentifier> {

    private final AssetManager assetManager;
    private final GameResourceCache resourceCache;
    private final SoundPool soundPool;
    private final Map<ResourceName, AndroidSoundIdentifier> loadedSounds;

    AndroidGameSoundSystem(GameResourceCache aResourceCache, AssetManager aAssetManager) {
        resourceCache = aResourceCache;
        assetManager = aAssetManager;
        soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
        loadedSounds = new HashMap<>();
    }

    private synchronized AndroidSoundIdentifier loadSound(ResourceName aResourceName) throws IOException {
        AndroidSoundResource theSound = resourceCache.getResourceFor(aResourceName);
        AndroidSoundIdentifier theIdentifier = loadedSounds.get(theSound.resourceName);
        if (theIdentifier == null) {
            int theSoundID = soundPool.load(assetManager.openFd(theSound.resourceName.name), 1);
            theIdentifier = new AndroidSoundIdentifier(theSoundID);
            loadedSounds.put(aResourceName, theIdentifier);
        }
        return theIdentifier;
    }

    @Override
    public AndroidStreamIdentifier play(ResourceName aResourceName) {
        try {
            AndroidSoundIdentifier theSoundIdentifer = loadSound(aResourceName);
            int theStreamID = soundPool.play(theSoundIdentifer.identifier, 1f, 1f, 1, 0, 1f);
            return new AndroidStreamIdentifier(theStreamID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop(AndroidStreamIdentifier aSoundObject) {
        soundPool.stop(aSoundObject.identifier);
    }
}
