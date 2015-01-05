package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.scriptengine.ScriptEngineFactory;

import java.util.List;

public class GameRuntime {

    private final GameEventManager eventManager;
    private GameSystem[] systems;
    private final GameResourceCache gameResourceCache;
    private final IORegistry ioRegistry;
    private final ExpressionParserFactory expressionParserFactory;
    private final ScriptEngineFactory scriptEngineFactory;

    public GameRuntime(GameEventManager aEventManager, GameResourceLoader aResourceLoader, ExpressionParserFactory aExpressionParserFactory, ScriptEngineFactory aScriptEngineFactory) {
        ioRegistry = new IORegistry();
        eventManager = aEventManager;
        systems = new GameSystem[0];
        gameResourceCache = new GameResourceCache(aResourceLoader);
        expressionParserFactory = aExpressionParserFactory;
        scriptEngineFactory = aScriptEngineFactory;
    }

    public GameResourceCache getResourceCache() {
        return gameResourceCache;
    }

    public GameEventManager getEventManager() {
        return eventManager;
    }

    public ExpressionParserFactory getExpressionParserFactory() {
        return expressionParserFactory;
    }

    public void addSystem(GameSystem aSystem) {
        List<GameSystem> theSystems = ArrayUtils.asList(systems);
        theSystems.add(aSystem);
        systems = theSystems.toArray(new GameSystem[theSystems.size()]);
    }

    public GameSystem[] getSystems() {
        return systems;
    }

    public IORegistry getIORegistry() {
        return ioRegistry;
    }

    public ScriptEngineFactory getScriptEngineFactory() {
        return scriptEngineFactory;
    }
}