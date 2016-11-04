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

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameResourceCache {

    private final Map<String, GameResource> cachedResources;
    private final GameResourceLoader resourceLoader;
    private LoadedSpriteSheet[] loadedSpriteSheets;

    public GameResourceCache(GameResourceLoader aResourceLoader) {
        cachedResources = new HashMap<>();
        resourceLoader = aResourceLoader;
        loadedSpriteSheets = new LoadedSpriteSheet[0];
    }

    public void loadIntoCache(Spritesheet aSheet, final SuccessCallback aCallback) {
        resourceLoader.loadSpriteSheet(aSheet.jsonFileProperty().get(),
                new GameResourceLoader.SpritesheetListener<GameResource>() {
                    @Override
                    public void handle(LoadedSpriteSheet<GameResource> aSpriteSheet) {
                        List<LoadedSpriteSheet> theSheets = ArrayUtils.asList(loadedSpriteSheets);
                        theSheets.add(aSpriteSheet);
                        loadedSpriteSheets = theSheets.toArray(new LoadedSpriteSheet[theSheets.size()]);
                        aCallback.success();
                    }
                });
    }

    public <T extends GameResource> void getResourceFor(final ResourceName aResourceName, final GameResourceLoader.Listener aListener) throws IOException {

        for (LoadedSpriteSheet theLoadedSheet : loadedSpriteSheets) {
            T theResource = (T) theLoadedSheet.getResourceFor(aResourceName);
            if (theResource != null) {
                aListener.handle(theResource);
            }
        }

        T theResource = (T) cachedResources.get(aResourceName.name);
        if (theResource == null) {
            resourceLoader.load(aResourceName, new GameResourceLoader.Listener() {
                @Override
                public void handle(GameResource aResource) {
                    aListener.handle(aResource);
                    cachedResources.put(aResourceName.name, aResource);
                }
            });
        }
    }

    public void flush() {
        resourceLoader.flush();
        cachedResources.clear();
    }
}