package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameScene;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PersistenceManager {

    @Inject
    Event<GameLoadedEvent> gameLoadedEventEvent;

    @Inject
    GameRuntimeFactory gameRuntimeFactory;

    private Game game;
    private Map<String, GameScene> gameScenes;
    private File currentGameDirectory;

    public PersistenceManager() {
        gameScenes = new HashMap<>();
    }

    public boolean isGameLoaded() {
        return game != null;
    }

    public Game getGame() {
        return game;
    }

    public GameScene getScene(String aSceneName) {
        return gameScenes.get(aSceneName);
    }

    public void loadGame(File aGameDirectory) throws IOException {
        File theGameDescriptor = new File(aGameDirectory, "game.json");
        ObjectMapper theObjectMapper = new ObjectMapper();
        ObjectReader theReader = theObjectMapper.reader(Map.class);
        Game theLoadedGame = Game.deserialize(theReader.<Map<String, Object>>readValue(theGameDescriptor));

        Map<String, GameScene> theLoadedScenes = new HashMap<>();
        for (String theSceneName : theLoadedGame.getScenes()) {
            File theSceneDescriptor = new File(new File(aGameDirectory,theSceneName),"scene.json");
            GameScene theLoadedScene = GameScene.deserialize(gameRuntimeFactory.createNewRuntime(), theReader.<Map<String, Object>>readValue(theSceneDescriptor));
            theLoadedScenes.put(theSceneName, theLoadedScene);
        }

        game = theLoadedGame;
        gameScenes = theLoadedScenes;
        gameLoadedEventEvent.fire(new GameLoadedEvent());
        currentGameDirectory = aGameDirectory;
    }
}
