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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.type.ResourceName;

public class GameResourceCache {

    private final Map<String, GameResource> cachedResources;
    private final GameResourceLoader resourceLoader;
    private final Logger logger;
    private LoadedSpriteSheet[] loadedSpriteSheets;

    public GameResourceCache(GameResourceLoader aResourceLoader, final Logger aLogger) {
        cachedResources = new HashMap<>();
        resourceLoader = aResourceLoader;
        loadedSpriteSheets = new LoadedSpriteSheet[0];
        logger = aLogger;
    }

    public Promise<Spritesheet, String> loadIntoCache(final Spritesheet aSheet) {
        logger.info("Loading sprite sheet into cache");
        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(final PromiseResolver aResolver, PromiseRejector aRejector) {
                logger.info("Delegating to resource loader");
                resourceLoader.loadSpriteSheet(aSheet.jsonFileProperty().get()).thenContinue(new Promise.NoReturnHandler<LoadedSpriteSheet>() {
                    @Override
                    public void process(LoadedSpriteSheet aResult) {
                        logger.info("Got sprite sheet for cache");
                        List<LoadedSpriteSheet> theSheets = ArrayUtils.asList(loadedSpriteSheets);
                        theSheets.add(aResult);
                        loadedSpriteSheets = theSheets.toArray(new LoadedSpriteSheet[theSheets.size()]);
                        aResolver.resolve(aSheet);
                    }
                });
            }
        });
    }

    public <T extends GameResource> Promise<T, String> getResourceFor(final ResourceName aResourceName) {

        return new Promise<T, String>(new Promise.Executor() {
            @Override
            public void process(final PromiseResolver aResolver, final PromiseRejector aRejector) {
                for (LoadedSpriteSheet theLoadedSheet : loadedSpriteSheets) {
                    T theResource = (T) theLoadedSheet.getResourceFor(aResourceName);
                    if (theResource != null) {
                        aResolver.resolve(theResource);
                        return;
                    }
                }

                T theResource = (T) cachedResources.get(aResourceName.name);
                if (theResource == null) {
                    resourceLoader.load(aResourceName).thenContinue(new Promise.NoReturnHandler<GameResource>() {
                        @Override
                        public void process(GameResource aResult) {
                            cachedResources.put(aResourceName.name, aResult);
                            aResolver.resolve(aResult);
                        }
                    }).catchError(new Promise.ErrorHandler<String>() {
                        @Override
                        public void process(String aResult, Exception aOptionalRejectedException) {
                            aRejector.reject(aResult, aOptionalRejectedException);
                        }
                    });
                } else {
                    aResolver.resolve(theResource);
                }
            }
        });
    }

    public void flush() {
        resourceLoader.flush();
        cachedResources.clear();
    }
}