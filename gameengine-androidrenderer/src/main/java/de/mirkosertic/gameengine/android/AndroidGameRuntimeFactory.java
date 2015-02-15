package de.mirkosertic.gameengine.android;

import android.util.Log;
import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.SystemException;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.luaj.LuaJScriptEngineFactory;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;

class AndroidGameRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    public GameRuntime create(GameResourceLoader aResourceLoader, GameSoundSystemFactory aSoundSystemFactory) {
        GameRuntime theRuntime = super.create(aResourceLoader, aSoundSystemFactory);
        theRuntime.getEventManager().register(null, SystemException.class, new GameEventListener<SystemException>() {
            @Override
            public void handleGameEvent(SystemException aEvent) {
                Log.wtf("SystemException", "SystemException", aEvent.exception);
            }
        });
        return theRuntime;
    }

    @Override
    protected LuaJScriptEngineFactory createScriptEngine() {
        return new LuaJScriptEngineFactory(new JDKBuiltInFunctions());
    }

    @Override
    protected JBox2DGamePhysicsManagerFactory createPhysicsManagerFactory() {
        return new JBox2DGamePhysicsManagerFactory();
    }
}
