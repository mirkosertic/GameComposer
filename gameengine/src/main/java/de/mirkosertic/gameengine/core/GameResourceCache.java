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
package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameResourceCache {

    private final Map<String, GameResource> cachedResources;
    private final GameResourceLoader resourceLoader;

    public GameResourceCache(GameResourceLoader aResourceLoader) {
        cachedResources = new HashMap<>();
        resourceLoader = aResourceLoader;
    }

    public <T extends GameResource> T getResourceFor(ResourceName aResourceName) throws IOException {
        T theResource = (T) cachedResources.get(aResourceName.name);
        if (theResource == null) {
            theResource = (T) resourceLoader.load(aResourceName);
            if (theResource == null) {
                throw new IOException("Cannot load resource " + aResourceName.name);
            }
            cachedResources.put(aResourceName.name, theResource);
            return theResource;
        }
        return theResource;
    }

    public void flush() {
        resourceLoader.flush();
        cachedResources.clear();
    }
}
