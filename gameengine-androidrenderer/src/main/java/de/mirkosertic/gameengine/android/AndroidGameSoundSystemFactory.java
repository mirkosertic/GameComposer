package de.mirkosertic.gameengine.android;

import android.content.res.AssetManager;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;

public class AndroidGameSoundSystemFactory implements GameSoundSystemFactory<AndroidStreamIdentifier> {

    private final AssetManager assetManager;

    AndroidGameSoundSystemFactory(AssetManager aAssetManager) {
        assetManager = aAssetManager;
    }

    @Override
    public GameSoundSystem<AndroidStreamIdentifier> create(GameRuntime aGameRuntime) {
        return new AndroidGameSoundSystem(aGameRuntime.getResourceCache());
    }
}
