package de.mirkosertic.gameengine.scriptengine;

import de.mirkosertic.gameengine.type.KeyValueObjectCache;
import de.mirkosertic.gameengine.type.Script;
import de.mirkosertic.gameengine.type.TextExpression;

import java.io.IOException;

public interface LUAScriptEngineFactory {

    LUAScriptEngine createNewEngine(KeyValueObjectCache aObjectCache, Script aScript) throws IOException;

    LUAScriptEngine createNewEngine(KeyValueObjectCache aObjectCache, TextExpression aExpression) throws IOException;
}
