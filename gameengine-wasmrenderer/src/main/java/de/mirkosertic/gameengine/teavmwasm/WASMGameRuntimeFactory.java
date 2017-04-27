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

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameSystemWork;
import de.mirkosertic.gameengine.core.Logger;
import de.mirkosertic.gameengine.core.ThreadingManager;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.physic.GamePhysicsManager;
import de.mirkosertic.gameengine.physic.GamePhysicsManagerFactory;
import de.mirkosertic.gameengine.physic.PhysicsDebugCanvas;
import de.mirkosertic.gameengine.process.GameProcess;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngine;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngineFactory;
import de.mirkosertic.gameengine.type.KeyValueObjectCache;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Script;
import de.mirkosertic.gameengine.type.TextExpression;

import java.io.IOException;

public class WASMGameRuntimeFactory extends AbstractGameRuntimeFactory {

    public static class DoNothingScriptEngine implements LUAScriptEngine {

        private final String script;

        public DoNothingScriptEngine(String aScript) {
            script = aScript;
        }

        @Override
        public void shutdown() {
        }

        @Override
        public void registerObject(String aObjectName, Reflectable aObject) {
        }

        @Override
        public Object proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
            return GameProcess.ProceedResult.STOPPED;
        }

        @Override
        public void registerPrimitive(String aObjectName, long aValue) {
        }

        @Override
        public String evaluateSimpleExpressionFor(GameObjectInstance aObjectInstance) {
            return script;
        }
    }

    @Override
    protected Logger createLogger() {
        return WASMLogger.INSTANCE;
    }

    @Override
    protected ThreadingManager createThreadingManager() {
        return new WASMThreadingManager();
    }

    @Override
    protected LUAScriptEngineFactory createScriptEngine() {
        return new LUAScriptEngineFactory() {
            @Override
            public LUAScriptEngine createNewEngine(KeyValueObjectCache aObjectCache, Script aScript)
                    throws IOException {
                return new DoNothingScriptEngine(aScript.script);
            }

            @Override
            public LUAScriptEngine createNewEngine(KeyValueObjectCache aObjectCache, TextExpression aExpression)
                    throws IOException {
                return new DoNothingScriptEngine(aExpression.expression);
            }
        };
    }

    @Override
    protected GamePhysicsManagerFactory createPhysicsManagerFactory() {
        return new GamePhysicsManagerFactory() {
            @Override
            public GamePhysicsManager create(Logger aLogger, GameEventManager aEventManager, ThreadingManager aThreadingManager) {
                return new GamePhysicsManager() {
                    @Override
                    public void drawDebug(PhysicsDebugCanvas aCanvas) {
                    }

                    @Override
                    public GameSystemWork proceedGame(long aTotalTicks, long aGameTime, long aElapsedTime) {
                        return new GameSystemWork() {
                            @Override
                            public void runInFrame() {
                            }

                            @Override
                            public void runAfterFrame() {
                            }
                        };
                    }
                };
            }
        };
    }
}
