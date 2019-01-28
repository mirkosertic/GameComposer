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

import java.util.Map;

import de.mirkosertic.bytecoder.api.web.Response;
import de.mirkosertic.bytecoder.api.web.StringPromise;
import de.mirkosertic.bytecoder.api.web.WindowOrWorkerGlobalScope;
import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.core.PromiseRejector;
import de.mirkosertic.gameengine.core.PromiseResolver;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;

public class BytecoderGameSceneLoader {

    private final WindowOrWorkerGlobalScope windowOrWorkerGlobalScope;
    private final AbstractGameRuntimeFactory runtimeFactory;
    private final GameSoundSystemFactory soundSystemFactory;
    private final JSONParser jsonParser;

    public BytecoderGameSceneLoader(WindowOrWorkerGlobalScope windowOrWorkerGlobalScope, AbstractGameRuntimeFactory runtimeFactory,
            GameSoundSystemFactory soundSystemFactory) {
        this.runtimeFactory = runtimeFactory;
        this.windowOrWorkerGlobalScope = windowOrWorkerGlobalScope;
        this.soundSystemFactory = soundSystemFactory;
        this.jsonParser = new JSONParser();
    }

    private void loadGameAsText(Response asText, String aSceneName, PromiseResolver aResolver, Game aGame) {
        asText.text().then(new StringPromise.Handler() {
            @Override
            public void handleString(String aValue) {

                BytecoderLogger.INSTANCE.info("Got text for game scene : " + aValue);

                Map<String, Object> theData = jsonParser.fromJSON(aValue);
                GameRuntime runtime = runtimeFactory.create(new BytecoderGameResourceLoader(aSceneName),
                        soundSystemFactory);

                BytecoderLogger.INSTANCE.info("New game runtime created");

                aResolver.resolve(GameScene.deserialize(aGame, runtime, theData));

                BytecoderLogger.INSTANCE.info("Done loading game scene!");
            }
        });
    }

    public Promise<GameScene, String> loadFromServer(final Game aGame, String aSceneName, final BytecoderGameResourceLoader aResourceLoader) {
        BytecoderLogger.INSTANCE.info("Loading game scene " + aSceneName);
        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
                windowOrWorkerGlobalScope.fetch(aSceneName + "/scene.json").then(new de.mirkosertic.bytecoder.api.web.Promise.Handler<Response>() {
                    @Override
                    public void handleObject(Response aValue) {
                        loadGameAsText(aValue, aSceneName, aResolver, aGame);
                    }
                });
            }
        });
    }
}
