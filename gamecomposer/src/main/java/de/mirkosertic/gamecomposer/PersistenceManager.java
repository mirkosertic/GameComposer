/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventType;
import de.mirkosertic.gameengine.javafx.JavaSoundAPISoundSystemFactory;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;

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

    @Inject
    PreviewGameRuntimeFactory previewGameRuntimeFactory;

    @Inject
    Event<StatusEvent> statusEvent;

    private Game game;
    private Map<String, GameScene> gameScenes;
    private File currentGameDirectory;

    public PersistenceManager() {
        gameScenes = new HashMap<>();
    }

    public void onNewGameEvent(@Observes NewGameEvent aEvent) {
        currentGameDirectory = aEvent.getProjectDirectory();
        game = aEvent.getGame();

        try {
            File theGameDescriptor = new File(currentGameDirectory, "game.json");
            ObjectMapper theObjectMapper = new ObjectMapper();
            ObjectWriter theWriter = theObjectMapper.writerWithDefaultPrettyPrinter().withType(Map.class);
            theWriter.writeValue(theGameDescriptor, game.serialize());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        // We also create a default assets folder
        File theAssets = new File(theSceneDirectory, "assets");
        new File(theAssets, "sprites").mkdirs();
        new File(theAssets, "sounds").mkdirs();
        new File(theAssets, "backgrounds").mkdirs();

        GameResourceLoader theResourceLoader = new JavaFXFileGameResourceLoader(theSceneDirectory);
        GameRuntime theRuntime = gameRuntimeFactory.create(theResourceLoader, new JavaSoundAPISoundSystemFactory());

        GameScene theNewGameScene = new GameScene(game, theRuntime);
        gameScenes.put(theSceneID, theNewGameScene);

        if (game.defaultSceneProperty().isNull()) {
            game.defaultSceneProperty().set(theSceneID);
        }

        theRuntime.getEventManager().register(null, GameEventType.CATCH_ALL, new EventCDIForwarder());

        statusEvent.fire(new StatusEvent("New scene created at " + theSceneDirectory, StatusEvent.Severity.INFO));

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

        statusEvent.fire(new StatusEvent("Game saved", StatusEvent.Severity.INFO));
    }

    public void onLoadGame(@Observes LoadGameEvent aEvent) throws IOException {
        File theGameDirectory = aEvent.getGameFile().getParentFile();
        ObjectMapper theObjectMapper = new ObjectMapper();
        ObjectReader theReader = theObjectMapper.reader(Map.class);
        Game theLoadedGame = Game.deserialize(theReader.<Map<String, Object>>readValue(aEvent.getGameFile()));

        game = theLoadedGame;

        Map<String, GameScene> theLoadedScenes = new HashMap<>();

        // We detect scenes by scanning the directory for subdirectories with a game scene definition
        // this is much better than storing the list of scenes in the game.json
        // as this might lead to inconsistancies.
        for (File theFile : theGameDirectory.listFiles()) {
            if (theFile.isDirectory()) {
                File theSceneDescriptor = new File(theFile, "scene.json");
                if (theSceneDescriptor.exists()) {

                    String theSceneName = theFile.getName();

                    GameResourceLoader theResourceLoader = new JavaFXFileGameResourceLoader(theFile);

                    Map<String, Object> theSerializedData = theReader.readValue(theSceneDescriptor);

                    GameScene theLoadedScene = GameScene.deserialize(game, gameRuntimeFactory.create(theResourceLoader, new JavaSoundAPISoundSystemFactory()), theSerializedData);

                    gameRuntimeFactory.loadingFinished(theLoadedScene);

                    // Ok, we are done here
                    theLoadedScenes.put(theSceneName, theLoadedScene);
                }
            }
        }

        gameScenes = theLoadedScenes;
        eventGateway.fire(new GameLoadedEvent());
        currentGameDirectory = theGameDirectory;

        for (Map.Entry<String, GameScene> theEntry : gameScenes.entrySet()) {
            theEntry.getValue().getRuntime().getEventManager().register(null, GameEventType.CATCH_ALL, new EventCDIForwarder());
        }

        statusEvent.fire(new StatusEvent("Game loaded", StatusEvent.Severity.INFO));
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

    public void deleteScene(GameScene aGameScene) {
        for (Map.Entry<String, GameScene> theEntry : gameScenes.entrySet()) {
            if (theEntry.getValue() == aGameScene) {
                game.removeScene(aGameScene.nameProperty().get());
                File theSceneDirectory = new File(currentGameDirectory, theEntry.getKey());
                try {
                    FileUtils.deleteDirectory(theSceneDirectory);

                    statusEvent.fire(new StatusEvent("Scene deleted", StatusEvent.Severity.INFO));
                } catch (IOException e) {

                    statusEvent.fire(new StatusEvent("Cannot delete scene : " + e.getMessage(), StatusEvent.Severity.ERROR));

                    throw new RuntimeException(e);
                }

                gameScenes.remove(theEntry.getKey());
                return;
            }
        }
    }

    public Set<String> getScenes() {
        return Collections.unmodifiableSet(gameScenes.keySet());
    }

    public GameScene cloneSceneForPreview(GameScene aGameScene) {

        for (Map.Entry<String, GameScene> theEntry : gameScenes.entrySet()) {
            if (theEntry.getValue() == aGameScene) {

                File theSceneDirectory = new File(currentGameDirectory, theEntry.getKey());

                GameResourceLoader theResourceLoader = new JavaFXFileGameResourceLoader(theSceneDirectory);

                Map<String, Object> theSerializedData = aGameScene.serialize();
                Map<String, Object> theSerializedGame = aGameScene.getGame().serialize();

                Game theClonedGame = Game.deserialize(theSerializedGame);

                GameScene theLoadedScene = GameScene.deserialize(theClonedGame, previewGameRuntimeFactory.create(theResourceLoader, new JavaSoundAPISoundSystemFactory()), theSerializedData);

                previewGameRuntimeFactory.loadingFinished(theLoadedScene);

                return theLoadedScene;
            }
        }
        throw new IllegalArgumentException("Unknown scene : "+aGameScene);
    }

    public File getCurrentGameDirectory() {
        return currentGameDirectory;
    }
}