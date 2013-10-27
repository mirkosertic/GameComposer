package de.mirkosertic.gameengine.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameRuntime {

    private GameEventManager eventManager;
    private Set<GameSystem> systems;

    public GameRuntime(GameEventManager aEventManager) {
        eventManager = aEventManager;
        systems = new HashSet<GameSystem>();
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
}