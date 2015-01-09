package de.mirkosertic.gameengine.scriptengine;

import de.mirkosertic.gameengine.type.Script;
import de.mirkosertic.gameengine.type.TextExpression;

import java.io.IOException;

public interface ScriptEngineFactory {

    ScriptEngine createNewEngine(Script aScript) throws IOException;

    ScriptEngine createNewEngine(TextExpression aExpression) throws IOException;
}
