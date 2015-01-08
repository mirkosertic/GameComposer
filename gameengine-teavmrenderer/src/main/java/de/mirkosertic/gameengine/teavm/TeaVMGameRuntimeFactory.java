package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.lua.LuaScriptEngineFactory;
import de.mirkosertic.gameengine.type.Reflectable;

public class TeaVMGameRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    protected Reflectable createBuildInFunctions() {
        return new TeaVMBuildInFunctions();
    }

    @Override
    protected LuaScriptEngineFactory createScriptEngine() {
        return new LuaScriptEngineFactory();
    }

    @Override
    protected JBox2DGamePhysicsManagerFactory createPhysicsManagerFactory() {
        return new JBox2DGamePhysicsManagerFactory();
    }
}