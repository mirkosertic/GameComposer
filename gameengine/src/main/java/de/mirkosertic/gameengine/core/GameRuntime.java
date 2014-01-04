package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.List;

public class GameRuntime {

    private final GameEventManager eventManager;
    private GameSystem[] systems;
    private final GameResourceCache gameResourceCache;
    private final IORegistry ioRegistry;
    private final ExpressionParserFactory expressionParserFactory;

    public GameRuntime(GameEventManager aEventManager, GameResourceLoader aResourceLoader) {
        ioRegistry = new IORegistry();
        eventManager = aEventManager;
        systems = new GameSystem[0];
        gameResourceCache = new GameResourceCache(aResourceLoader);
        expressionParserFactory = new ExpressionParserFactory();
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
}