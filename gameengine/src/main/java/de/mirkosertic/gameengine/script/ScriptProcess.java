package de.mirkosertic.gameengine.script;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.process.GameProcess;
import de.mirkosertic.gameengine.scriptengine.ScriptEngine;
import de.mirkosertic.gameengine.scriptengine.ScriptEngineFactory;
import de.mirkosertic.gameengine.type.Script;

import java.io.IOException;

public class ScriptProcess implements GameProcess {

    private final GameObjectInstance instance;
    private final ScriptEngineFactory scriptEngineFactory;
    private final Script script;

    private ScriptEngine scriptEngine;

    public ScriptProcess(GameObjectInstance aInstance, ScriptEngineFactory aScriptEngineFactory, Script aScript) {
        instance = aInstance;
        scriptEngineFactory = aScriptEngineFactory;
        script = aScript;
    }

    @Override
    public void started() {
        if (scriptEngine == null) {
            try {
                scriptEngine = scriptEngineFactory.createNewEngine(script);
                scriptEngine.registerObject("instance", instance);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalStateException("Process already initialized");
        }
    }

    @Override
    public boolean affectsInstance(GameObjectInstance aInstance) {
        return instance == aInstance;
    }

    @Override
    public ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        Object theResult = scriptEngine.proceedGame(aGameTime, aElapsedTimeSinceLastLoop);
        if (theResult instanceof String) {
            return ProceedResult.valueOf((String) theResult);
        }
        if (theResult instanceof ProceedResult) {
            return (ProceedResult) theResult;
        }
        return ProceedResult.STOPPED;
    }

    @Override
    public void killed() {
        if (scriptEngine != null) {
            scriptEngine.shutdown();
        }
    }

    @Override
    public GameProcess getChildProcess() {
        return null;
    }
}