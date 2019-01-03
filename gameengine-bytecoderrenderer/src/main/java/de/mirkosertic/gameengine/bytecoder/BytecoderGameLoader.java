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
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.core.PromiseRejector;
import de.mirkosertic.gameengine.core.PromiseResolver;

public class BytecoderGameLoader {

    private final WindowOrWorkerGlobalScope globalScope;
    private final JSONParser jsonParser;

    public BytecoderGameLoader(WindowOrWorkerGlobalScope globalScope) {
        this.globalScope = globalScope;
        this.jsonParser = new JSONParser();
    }

    public Promise<Game, String> loadFromServer() {
        BytecoderLogger.INSTANCE.info("Loading game");

        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
                globalScope.fetch("game.json").then(new de.mirkosertic.bytecoder.api.web.Promise.Handler<Response>() {
                    @Override
                    public void handleObject(Response aValue) {
                        aValue.text().then(new StringPromise.Handler() {
                            @Override
                            public void handleString(String aValue) {

                                BytecoderLogger.INSTANCE.info("Got Game JSON Data");

                                final Map<String, Object> theData = jsonParser.fromJSON(aValue);
                                aResolver.resolve(Game.deserialize(theData));

                                BytecoderLogger.INSTANCE.info("Done loading game!");
                            }
                        });
                    }
                });
            }
        });
    }
}