package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameRuntime {

    private final GameEventManager eventManager;
    private final Set<GameSystem> systems;
    private final GameResourceCache gameResourceCache;
    private final IORegistry ioRegistry;

    private Game loadedGame;
    private GameScene loadedGameScene;

    public GameRuntime(GameEventManager aEventManager, GameResourceLoader aResourceLoader) {
        ioRegistry = new IORegistry();
        eventManager = aEventManager;
        systems = new HashSet<GameSystem>();
        gameResourceCache = new GameResourceCache(aResourceLoader);
    }

    public void loadGame(Map<String, Object> aSerialitzedData) {
        loadedGame = Game.deserialize(aSerialitzedData);
    }

    public Game getLoadedGame() {
        return loadedGame;
    }

    public void loadGameScene(Map<String, Object> aSceneData) {
        loadedGameScene =  GameScene.deserialize(this, aSceneData);
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