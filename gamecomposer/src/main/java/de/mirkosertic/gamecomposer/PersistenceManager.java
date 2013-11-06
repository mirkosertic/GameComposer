package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.ResourceName;
import de.mirkosertic.gameengine.core.GameResourceLoader;
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

            GameResourceLoader theResourceLoader = new JavaFXFileGameResourceLoader(new File(aGameDirectory, theSceneName));

            File theSceneDescriptor = new File(new File(aGameDirectory, theSceneName), "scene.json");
            GameScene theLoadedScene = GameScene.deserialize(gameRuntimeFactory.create(theResourceLoader), theReader.<Map<String, Object>>readValue(theSceneDescriptor));
            theLoadedScenes.put(theSceneName, theLoadedScene);
        }

        game = theLoadedGame;
        gameScenes = theLoadedScenes;
        gameLoadedEventEvent.fire(new GameLoadedEvent());
        currentGameDirectory = aGameDirectory;
    }


    public GameResourceLoader createResourceLoaderFor(GameScene aScene) {
        for (Map.Entry<String, GameScene> theEntry : gameScenes.entrySet()) {
            if (theEntry.getValue() == aScene) {
                return new JavaFXFileGameResourceLoader(new File(currentGameDirectory, theEntry.getKey()));
            }
        }
        throw new RuntimeException("Cannot find scene directory for " + aScene.getName());
    }

    public File getAssetsDirectoryFor(GameScene aGameScene) {
        for (Map.Entry<String, GameScene> theEntry : gameScenes.entrySet()) {
            if (theEntry.getValue() == aGameScene) {
                return new File(currentGameDirectory, theEntry.getKey());
            }
        }
        throw new RuntimeException("Cannot find scene directory for " + aGameScene.getName());
    }

    public ResourceName toResourceName(GameScene aGameScene, File theSelectedFile) {
        for (Map.Entry<String, GameScene> theEntry : gameScenes.entrySet()) {
            if (theEntry.getValue() == aGameScene) {
                File theBaseDirectory = new File(currentGameDirectory, theEntry.getKey());
                String theResourceName = theSelectedFile.toString().substring(theBaseDirectory.toString().length());
                return new ResourceName(theResourceName.replace('\\','/'));
            }
        }
        throw new RuntimeException("Cannot find scene directory for " + aGameScene.getName());
    }
}
