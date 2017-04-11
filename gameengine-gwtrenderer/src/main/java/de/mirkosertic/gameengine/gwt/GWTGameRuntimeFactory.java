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
package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.NoThreadingThreadingManager;
import de.mirkosertic.gameengine.core.ThreadingManager;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.SystemException;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.luaj.LuaJScriptEngineFactory;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;

import com.google.gwt.core.client.GWT;

class GWTGameRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    public GameRuntime create(GameResourceLoader aResourceLoader, GameSoundSystemFactory aSoundSystemFactory) {
        GameRuntime theRuntime = super.create(aResourceLoader, aSoundSystemFactory);
        theRuntime.getEventManager().register(null, SystemException.TYPE, new GameEventListener<SystemException>() {
            @Override
            public void handleGameEvent(SystemException aEvent) {
                GWT.log("System exception : " + aEvent.exception.getMessage(), aEvent.exception);
            }
        });
        return theRuntime;
    }

    @Override
    protected ThreadingManager createThreadingManager() {
        return new NoThreadingThreadingManager();
    }

    @Override
    protected LuaJScriptEngineFactory createScriptEngine() {
        return new LuaJScriptEngineFactory(new GWTBuiltInFunctions());
    }

    @Override
    protected JBox2DGamePhysicsManagerFactory createPhysicsManagerFactory() {
        return new JBox2DGamePhysicsManagerFactory();
    }
}