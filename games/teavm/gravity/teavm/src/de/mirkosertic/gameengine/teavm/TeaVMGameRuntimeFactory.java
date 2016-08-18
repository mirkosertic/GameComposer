package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Logger;
import de.mirkosertic.gameengine.core.NoThreadingThreadingManager;
import de.mirkosertic.gameengine.core.ThreadingManager;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.luaj.LuaJScriptEngineFactory;

public class TeaVMGameRuntimeFactory extends AbstractGameRuntimeFactory {

    private final boolean multithreaded;
    private final boolean profiling;

    public TeaVMGameRuntimeFactory(boolean aMultithreaded, boolean aProfiling) {
        multithreaded = aMultithreaded;
        profiling = aProfiling;
    }

    @Override
    protected Logger createLogger() {
        return new Logger() {
            @Override
            public void info(String aMessage) {
                TeaVMLogger.info(aMessage);
            }

            @Override
            public void error(String aMessage) {
                TeaVMLogger.error(aMessage);
            }

            @Override
            public void time(String aLabel) {
                if (profiling) {
                    TeaVMLogger.time(aLabel);
                }
            }

            @Override
            public void timeEnd(String aLabel) {
                if (profiling) {
                    TeaVMLogger.timeEnd(aLabel);
                }
            }
        };
    }

    @Override
    protected ThreadingManager createThreadingManager() {
        if (multithreaded) {
            return new TeaVMThreadingManager();
        }
        return new NoThreadingThreadingManager();
    }

    @Override
    protected LuaJScriptEngineFactory createScriptEngine() {
        return new LuaJScriptEngineFactory(new TeaVMBuildInFunctions());
    }

    @Override
    protected JBox2DGamePhysicsManagerFactory createPhysicsManagerFactory() {
        return new JBox2DGamePhysicsManagerFactory();
    }
}