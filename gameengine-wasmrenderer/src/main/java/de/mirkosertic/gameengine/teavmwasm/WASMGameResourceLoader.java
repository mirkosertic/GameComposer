/*
 * Copyright 2017 Mirko Sertic
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
package de.mirkosertic.gameengine.teavmwasm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.core.PromiseRejector;
import de.mirkosertic.gameengine.core.PromiseResolver;
import de.mirkosertic.gameengine.type.ResourceName;

public class WASMGameResourceLoader implements GameResourceLoader {

    @Override
    public Promise<GameResource, String> load(ResourceName aResourceName) {
        WASMLogger.INSTANCE.info("Loading Game Resource " +  aResourceName);
        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {

            }
        });
    }

    @Override
    public Promise<LoadedSpriteSheet, String> loadSpriteSheet(ResourceName aResourceName) {
        WASMLogger.INSTANCE.info("Loading Style sheet " +  aResourceName);
        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
            }
        });
    }

    @Override
    public void flush() {
    }
}
