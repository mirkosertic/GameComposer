package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.lua.LuaScriptEngineFactory;

public class DragomeRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    protected DragomeBuildInFunctions createBuildInFunctions() {
        return new DragomeBuildInFunctions();
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
