package de.mirkosertic.gameengine.scriptengine;

import de.mirkosertic.gameengine.type.Reflectable;

public interface ScriptEngine {

    void shutdown();

    void registerObject(String aObjectName, Reflectable aObject);

    Object proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop);

    void registerPrimitive(String aObjectName, long aValue);
}