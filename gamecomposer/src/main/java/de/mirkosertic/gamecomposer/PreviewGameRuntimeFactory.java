package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.javafx.JDKBuiltInFunctions;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.luaj.LuaJScriptEngineFactory;
import de.mirkosertic.gameengine.type.Reflectable;

import javax.inject.Singleton;

@Singleton
public class PreviewGameRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    protected Reflectable createBuildInFunctions() {
        return new JDKBuiltInFunctions();
    }

    @Override
    protected LuaJScriptEngineFactory createScriptEngine() {
        return new LuaJScriptEngineFactory();
    }

    @Override
    protected JBox2DGamePhysicsManagerFactory createPhysicsManagerFactory() {
        return new JBox2DGamePhysicsManagerFactory();
    }
}