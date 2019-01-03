/*
 * Copyright 2019 Mirko Sertic
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
package de.mirkosertic.gameengine.bytecoder;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.core.PromiseRejector;
import de.mirkosertic.gameengine.core.PromiseResolver;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.type.ResourceName;

public class BytecoderGameSoundSystem implements GameSoundSystem {

    private final GameRuntime gameRuntime;

    public BytecoderGameSoundSystem(GameRuntime gameRuntime) {
        this.gameRuntime = gameRuntime;
    }

    @Override
    public Promise play(ResourceName aResourceName) {
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
