package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.javafx.JDKBuiltInFunctions;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.lua.LuaScriptEngineFactory;
import de.mirkosertic.gameengine.type.Reflectable;

import javax.inject.Singleton;

@Singleton
class GameRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    public void loadingFinished(GameScene aLoadesScene) {
        // We do not need an action manager here, so this method is overwritten
        //super.loadingFinished(aLoadesScene);
    }

    @Override
    protected Reflectable createBuildInFunctions() {
        return new JDKBuiltInFunctions();
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