package de.mirkosertic.gameengine.scriptengine;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.type.Reflectable;

public interface LUAScriptEngine {

    void shutdown();

    void registerObject(String aObjectName, Reflectable aObject);

    Object proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop);

    void registerPrimitive(String aObjectName, long aValue);

    String evaluateSimpleExpressionFor(GameObjectInstance aObjectInstance);
}