/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.android;

import android.media.AudioManager;
import android.media.SoundPool;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceCache;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AndroidGameSoundSystem implements GameSoundSystem<AndroidStreamIdentifier> {

    private final GameResourceCache resourceCache;
    private final SoundPool soundPool;
    private final Map<ResourceName, AndroidSoundIdentifier> loadedSounds;
    private final Set<Integer> loadedIDs;

    AndroidGameSoundSystem(GameResourceCache aResourceCache) {
        resourceCache = aResourceCache;
        loadedIDs = new HashSet<>();
        soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool aSoundPool, int aSampleID, int aStatus) {
                loadedIDs.add(aSampleID);
                soundPool.play(aSampleID, 1f, 1f, 1, 0, 1f);
            }
        });
        loadedSounds = new HashMap<>();
    }

    @Override
    public void play(final ResourceName aResourceName, final Listener<AndroidStreamIdentifier> aListener) {
        try {
            resourceCache.getResourceFor(aResourceName, new GameResourceLoader.Listener() {
                @Override
                public void handle(GameResource aResource) {
                    AndroidSoundResource theResource = (AndroidSoundResource) aResource;
                    AndroidSoundIdentifier theIdentifier = loadedSounds.get(theResource.resourceName);
                    if (theIdentifier == null) {
                        int theSoundID = soundPool.load(theResource.fileDescriptor, 1);
                        theIdentifier = new AndroidSoundIdentifier(theSoundID);
                        loadedSounds.put(theResource.resourceName, theIdentifier);
                    }

                    int theStreamID = soundPool.play(theIdentifier.identifier, 1f, 1f, 1, 0, 1f);
                    aListener.handle(new AndroidStreamIdentifier(theStreamID));

                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop(AndroidStreamIdentifier aSoundObject) {
        soundPool.stop(aSoundObject.identifier);
    }
}