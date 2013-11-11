package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventListener;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PersistenceManager {

    class EventCDIForwarder<T extends GameEvent> implements GameEventListener<T> {
        @Override
        public void handleGameEvent(GameEvent aEvent) {
            eventGateway.fire(aEvent);
        }
    }

    @Inject
    Event<Object> eventGateway;

    @Inject
    GameRuntimeFactory gameRuntimeFactory;

    private Game game;
    private Map<String, GameScene> gameScenes;
    private File currentGameDirectory;

    public PersistenceManager() {
        gameScenes = new HashMap<>();
    }

    public void onNewGameEvent(@Observes NewGameEvent aEvent) {
        game = new Game();
        gameScenes.clear();
        currentGameDirectory = aEvent.getProjectDirectory();
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

    public void onNewGameScene(@Observes NewGameSceneEvent aEvent) {
        String theSceneID = "scene" + (gameScenes.size() + 1);

        File theSceneDirectory = new File(currentGameDirectory, theSceneID);
        theSceneDirectory.mkdirs();

        GameResourceLoader theResourceLoader = new JavaFXFileGameResourceLoader(theSceneDirectory);
        GameRuntime theRuntime = gameRuntimeFactory.create(theResourceLoader);

        GameScene theNewGameScene = new GameScene(theRuntime);
        gameScenes.put(theSceneID, theNewGameScene);

        if (game.getScenes().size() == 0) {
            game.defaultSceneProperty().set(theSceneID);
        }
        game.addScene(theSceneID);

        theRuntime.getEventManager().register(null, GameEvent.class, new EventCDIForwarder());

        eventGateway.fire(new GameSceneCreatedEvent(theNewGameScene));
    }

    public void onSaveGame(@Observes SaveGameEvent aEvent) throws IOException {
        File theGameDescriptor = new File(currentGameDirectory, "game.json");
        ObjectMapper theObjectMapper = new ObjectMapper();
        ObjectWriter theWriter = theObjectMapper.writerWithDefaultPrettyPrinter().withType(Map.class);
        theWriter.writeValue(theGameDescriptor, game.serialize());

        for (Map.Entry<String, GameScene> theSceneEntry : gameScenes.entrySet()) {
            File theSceneFile = new File(new File(currentGameDirectory, theSceneEntry.getKey()), "scene.json");
            theWriter.writeValue(theSceneFile, theSceneEntry.getValue().serialize());
        }
    }

    public void onLoadGame(@Observes LoadGameEvent aEvent) throws IOException {
        File theGameDirectory = aEvent.getGameFile().getParentFile();
        ObjectMapper theObjectMapper = new ObjectMapper();
        ObjectReader theReader = theObjectMapper.reader(Map.class);
        Game theLoadedGame = Game.deserialize(theReader.<Map<String, Object>>readValue(aEvent.getGameFile()));

        Map<String, GameScene> theLoadedScenes = new HashMap<>();
        for (String theSceneName : theLoadedGame.getScenes()) {

            GameResourceLoader theResourceLoader = new JavaFXFileGameResourceLoader(new File(theGameDirectory, theSceneName));

            File theSceneDescriptor = new File(new File(theGameDirectory, theSceneName), "scene.json");
            GameScene theLoadedScene = GameScene.deserialize(gameRuntimeFactory.create(theResourceLoader), theReader.<Map<String, Object>>readValue(theSceneDescriptor));
            theLoadedScenes.put(theSceneName, theLoadedScene);
        }

        game = theLoadedGame;
        gameScenes = theLoadedScenes;
        eventGateway.fire(new GameLoadedEvent());
        currentGameDirectory = theGameDirectory;

        for (Map.Entry<String, GameScene> theEntry : gameScenes.entrySet()) {
            theEntry.getValue().getRuntime().getEventManager().register(null, GameEvent.class, new EventCDIForwarder());
        }
    }

    public GameResourceLoader createResourceLoaderFor(GameScene aScene) {
        for (Map.Entry<String, GameScene> theEntry : gameScenes.entrySet()) {
            if (theEntry.getValue() == aScene) {
                return new JavaFXFileGameResourceLoader(new File(currentGameDirectory, theEntry.getKey()));
            }
        }
        throw new RuntimeException("Cannot find scene directory for " + aScene.nameProperty().get());
    }

    public File getAssetsDirectoryFor(GameScene aGameScene) {
        for (Map.Entry<String, GameScene> theEntry : gameScenes.entrySet()) {
            if (theEntry.getValue() == aGameScene) {
                return new File(currentGameDirectory, theEntry.getKey());
            }
        }
        throw new RuntimeException("Cannot find scene directory for " + aGameScene.nameProperty().get());
    }

    public ResourceName toResourceName(GameScene aGameScene, File theSelectedFile) {
        for (Map.Entry<String, GameScene> theEntry : gameScenes.entrySet()) {
            if (theEntry.getValue() == aGameScene) {
                File theBaseDirectory = new File(currentGameDirectory, theEntry.getKey());
                String theResourceName = theSelectedFile.toString().substring(theBaseDirectory.toString().length());
                return new ResourceName(theResourceName.replace('\\', '/'));
            }
        }
        throw new RuntimeException("Cannot find scene directory for " + aGameScene.nameProperty().get());
    }

    public void copyGameTo(File aTargetDirectory) throws IOException {
        FileUtils.copyDirectory(currentGameDirectory, aTargetDirectory);
    }
}