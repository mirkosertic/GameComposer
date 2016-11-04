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
package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.javascript.ScriptHelper;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceCache;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

public class DragomeGameSoundSystem implements GameSoundSystem<DragomeSound> {

    private final GameResourceCache resourceCache;

    public DragomeGameSoundSystem(GameResourceCache aResourceCache) {
        resourceCache = aResourceCache;
    }

    @Override
    public void play(ResourceName aResourceName, Listener<DragomeSound> aListener) {
        try {
            resourceCache.getResourceFor(aResourceName, new GameResourceLoader.Listener() {
                @Override
                public void handle(GameResource aResource) {
                    if (aResource.getType() == GameResourceType.SOUND) {
                        DragomeGameResource theResource = (DragomeGameResource) aResource;
                        ScriptHelper.put("sn", theResource.getName(), this);
                        ScriptHelper.evalNoResult("new Audio(sn).play()", this);

                        aListener.handle(new DragomeSound());
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop(DragomeSound aSoundObject) {
    }
}