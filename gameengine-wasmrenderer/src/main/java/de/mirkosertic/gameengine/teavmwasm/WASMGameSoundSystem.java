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

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.core.PromiseRejector;
import de.mirkosertic.gameengine.core.PromiseResolver;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

public class WASMGameSoundSystem implements GameSoundSystem {

    private final GameRuntime gameRuntime;

    public WASMGameSoundSystem(GameRuntime aGameRuntime) {
        gameRuntime = aGameRuntime;
    }

    @Override
    public Promise play(ResourceName aResourceName) {
        WASMLogger.INSTANCE.info("Playing sound " + aResourceName.get());
        return new Promise(new Promise.Executor() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
            }
        });
    }


    @Override
    public void stop(Object aSoundObject) {
    }
}
