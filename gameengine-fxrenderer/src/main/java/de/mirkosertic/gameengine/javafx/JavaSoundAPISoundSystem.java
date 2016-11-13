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
package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameResourceCache;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

import javax.sound.sampled.Clip;

public class JavaSoundAPISoundSystem implements GameSoundSystem<Clip> {

    private final GameResourceCache resourceCache;

    JavaSoundAPISoundSystem(GameResourceCache aResourceCache) {
        resourceCache = aResourceCache;
    }

    @Override
    public Promise<Clip, String> play(ResourceName aResourceName) {
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> resourceCache.getResourceFor(aResourceName).thenContinue(aResult -> {
            if (aResult.getType() == GameResourceType.SOUND) {
                Clip theClip = null;
                try {
                    theClip = ((JavaFXAudioResource) aResult).createClip();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                theClip.start();
                aResolver.resolve(theClip);
            }
        }));
    }

    @Override
    public void stop(Clip aSoundObject) {
        if (aSoundObject.isRunning()) {
            aSoundObject.stop();
        }
    }
}