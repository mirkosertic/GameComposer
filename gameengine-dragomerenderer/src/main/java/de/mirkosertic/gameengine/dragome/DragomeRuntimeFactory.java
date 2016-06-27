package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.NoThreadingThreadingManager;
import de.mirkosertic.gameengine.core.ThreadingManager;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.luaj.LuaJScriptEngineFactory;

public class DragomeRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    protected LuaJScriptEngineFactory createScriptEngine() {
        return new LuaJScriptEngineFactory(new DragomeBuildInFunctions());
    }

    @Override
    protected JBox2DGamePhysicsManagerFactory createPhysicsManagerFactory() {
        return new JBox2DGamePhysicsManagerFactory();
    }

    @Override
    protected ThreadingManager createThreadingManager() {
        return new NoThreadingThreadingManager();
    }
}
