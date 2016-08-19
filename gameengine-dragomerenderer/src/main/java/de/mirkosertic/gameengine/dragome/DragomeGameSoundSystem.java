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

import de.mirkosertic.gameengine.core.GameResourceCache;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

import com.dragome.commons.javascript.ScriptHelper;

public class DragomeGameSoundSystem implements GameSoundSystem<DragomeSound> {

    private final GameResourceCache resourceCache;

    public DragomeGameSoundSystem(GameResourceCache aResourceCache) {
        resourceCache = aResourceCache;
    }

    @Override
    public DragomeSound play(ResourceName aResourceName) {
        try {
            DragomeGameResource theResource = resourceCache.getResourceFor(aResourceName);
            if (theResource != null && theResource.getType() == GameResourceType.SOUND) {
                ScriptHelper.put("sn", theResource.getName(), this);
                ScriptHelper.evalNoResult("new Audio(sn).play()", this);
                return new DragomeSound();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void stop(DragomeSound aSoundObject) {
    }
}