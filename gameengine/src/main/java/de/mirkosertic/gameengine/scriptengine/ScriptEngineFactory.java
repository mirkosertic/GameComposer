package de.mirkosertic.gameengine.scriptengine;

import de.mirkosertic.gameengine.type.Script;

import java.io.IOException;

public interface ScriptEngineFactory {

    ScriptEngine createNewEngine(Script aScript) throws IOException;
}
