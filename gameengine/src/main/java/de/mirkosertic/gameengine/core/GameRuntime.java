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
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngineFactory;

import java.util.List;

public class GameRuntime {

    private final GameEventManager eventManager;
    private GameSystem[] systems;
    private final GameResourceCache gameResourceCache;
    private final IORegistry ioRegistry;
    private final LUAScriptEngineFactory scriptEngineFactory;
    private final Logger logger;

    public GameRuntime(GameEventManager aEventManager, GameResourceLoader aResourceLoader,
            LUAScriptEngineFactory aScriptEngineFactory, Logger aLogger) {
        ioRegistry = new IORegistry();
        logger = aLogger;
        eventManager = aEventManager;
        systems = new GameSystem[0];
        gameResourceCache = new GameResourceCache(aResourceLoader, aLogger);
        scriptEngineFactory = aScriptEngineFactory;
    }

    public Logger getLogger() {
        return logger;
    }

    public GameResourceCache getResourceCache() {
        return gameResourceCache;
    }

    public GameEventManager getEventManager() {
        return eventManager;
    }

    public void addSystem(GameSystem aSystem) {
        List<GameSystem> theSystems = ArrayUtils.asList(systems);
        theSystems.add(aSystem);
        systems = theSystems.toArray(new GameSystem[theSystems.size()]);
    }

    public GameSystem[] getSystems() {
        return systems;
    }

    public IORegistry getIORegistry() {
        return ioRegistry;
    }

    public LUAScriptEngineFactory getScriptEngineFactory() {
        return scriptEngineFactory;
    }
}