package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.ThreadingManager;
import de.mirkosertic.gameengine.javafx.JDKBuiltInFunctions;
import de.mirkosertic.gameengine.javafx.JDKThreadingManager;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.luaj.LuaJScriptEngineFactory;

import javax.inject.Singleton;

@Singleton
class GameRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    protected ThreadingManager createThreadingManager() {
        return new JDKThreadingManager();
    }

    @Override
    public void loadingFinished(GameScene aLoadesScene) {
        // We do not need an action manager here, so this method is overwritten
        //super.loadingFinished(aLoadesScene);
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