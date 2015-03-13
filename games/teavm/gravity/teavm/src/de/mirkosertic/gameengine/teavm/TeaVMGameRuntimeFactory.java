package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.luaj.LuaJScriptEngineFactory;

public class TeaVMGameRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    protected LuaJScriptEngineFactory createScriptEngine() {
        return new LuaJScriptEngineFactory(new TeaVMBuildInFunctions());
    }

    @Override
    protected JBox2DGamePhysicsManagerFactory createPhysicsManagerFactory() {
        return new JBox2DGamePhysicsManagerFactory();
    }
}