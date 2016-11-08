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
package de.mirkosertic.gameengine.script;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.process.GameProcess;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngine;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngineFactory;
import de.mirkosertic.gameengine.type.Script;

import java.io.IOException;

public class ScriptProcess implements GameProcess {

    private final GameScene scene;
    private GameObjectInstance instance;
    private final LUAScriptEngineFactory scriptEngineFactory;
    private final Script script;
    private final GameEvent causingEvent;

    private LUAScriptEngine scriptEngine;

    public ScriptProcess(GameEvent aCausingEvent, GameScene aScene, LUAScriptEngineFactory aScriptEngineFactory, Script aScript) {
        causingEvent = aCausingEvent;
        scene = aScene;
        scriptEngineFactory = aScriptEngineFactory;
        script = aScript;
    }

    public ScriptProcess(GameEvent aEvent, GameObjectInstance aInstance, LUAScriptEngineFactory aScriptEngineFactory, Script aScript) {
        this(aEvent, aInstance.getOwnerGameObject().getGameScene(), aScriptEngineFactory, aScript);
        instance = aInstance;
    }

    @Override
    public void started() {
        if (scriptEngine == null) {
            try {
                scriptEngine = scriptEngineFactory.createNewEngine(scene, script);
                if (instance != null) {
                    scriptEngine.registerObject("instance", instance);
                }
                if (causingEvent != null) {
                    scriptEngine.registerObject("event", causingEvent);
                }
                scriptEngine.registerObject("scene", scene);
                scriptEngine.registerObject("game", scene.getGame());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalStateException("Process already initialized");
        }
    }

    @Override
    public boolean affectsInstance(GameObjectInstance aInstance) {
        return instance == aInstance;
    }

    @Override
    public ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        Object theResult = scriptEngine.proceedGame(aGameTime, aElapsedTimeSinceLastLoop);
        if (theResult instanceof String) {
            return ProceedResult.valueOf((String) theResult);
        }
        if (theResult instanceof ProceedResult) {
            return (ProceedResult) theResult;
        }
        return ProceedResult.STOPPED;
    }

    @Override
    public void killed() {
        if (scriptEngine != null) {
            scriptEngine.shutdown();
        }
    }

    @Override
    public GameProcess getChildProcess() {
        return null;
    }
}