package de.mirkosertic.gameengine.scriptengine;

import de.mirkosertic.gameengine.type.Script;
import de.mirkosertic.gameengine.type.TextExpression;

import java.io.IOException;

public interface LUAScriptEngineFactory {

    LUAScriptEngine createNewEngine(Script aScript) throws IOException;

    LUAScriptEngine createNewEngine(TextExpression aExpression) throws IOException;
}
