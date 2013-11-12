package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameRuntime {

    private GameEventManager eventManager;
    private Set<GameSystem> systems;
    private GameResourceCache gameResourceCache;
    private IORegistry ioRegistry;

    public GameRuntime(GameEventManager aEventManager, GameResourceLoader aResourceLoader) {
        ioRegistry = new IORegistry();
        eventManager = aEventManager;
        systems = new HashSet<GameSystem>();
        gameResourceCache = new GameResourceCache(aResourceLoader);
    }

    public GameResourceCache getResourceCache() {
        return gameResourceCache;
    }

    public GameEventManager getEventManager() {
        return eventManager;
    }

    public void addSystem(GameSystem aSystem) {
        systems.add(aSystem);
    }

    public Set<GameSystem> getSystems() {
        return Collections.unmodifiableSet(systems);
    }

    public IORegistry getIORegistry() {
        return ioRegistry;
    }
}