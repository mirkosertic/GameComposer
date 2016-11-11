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
package de.mirkosertic.gameengine.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.json.JSON;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMMap;

public class EditorState {

    public interface WriteListener {

        void written();

        void error(String aMessage);
    }

    public interface LoadingListener {

        void onGameLoaded(EditorState aState);

        void onGameLoadingError(Throwable aThrowable);

        void onSceneLoaded(EditorState aState, String aSceneID);

        void onSceneLoadingError(EditorState aState, String aSceneID, Throwable aThrowable);
    }

    private Game loadedGame;
    private final Map<String, GameScene> loadedScenes;
    private final EditorProject editorProject;
    private final AbstractGameRuntimeFactory runtimeFactory;
    private final ResourceAccessor resourceAccessor;

    public EditorState(EditorProject aProject, AbstractGameRuntimeFactory aRuntimeFactory, ResourceAccessor aResourceAccessor) {
        loadedScenes = new HashMap<>();
        editorProject = aProject;
        runtimeFactory = aRuntimeFactory;
        resourceAccessor = aResourceAccessor;
    }

    public EditorProject getEditorProject() {
        return editorProject;
    }

    public AbstractGameRuntimeFactory getRuntimeFactory() {
        return runtimeFactory;
    }

    public Game getLoadedGame() {
        return loadedGame;
    }

    public GameScene getGameSceneById(String aSceneId) {
        return loadedScenes.get(aSceneId);
    }

    public void saveGame(WriteListener aListener) {
        JSObject theJSForm = TeaVMMap.toJS(loadedGame.serialize());
        String theJSON = JSON.stringify(theJSForm);

        Blob theBlob = Blob.createJSONBlob(JSString.valueOf(theJSON));
        resourceAccessor.persistFile("/game.json", theBlob, new ResourceAccessor.CompleteCallback() {
            @Override
            public void fileWritten() {
                aListener.written();
            }

            @Override
            public void error(String aMessage) {
                aListener.error(aMessage);
            }
        });
    }

    private String getIDForScene(GameScene aGameScene) {
        for (Map.Entry<String, GameScene> theEntry : loadedScenes.entrySet()) {
            if (theEntry.getValue() == aGameScene) {
                return theEntry.getKey();
            }
        }
        throw new IllegalStateException("Unknown game scane");
    }

    public void saveScene(GameScene aScene, WriteListener aListener) {
        saveGame(new WriteListener() {
            @Override
            public void written() {
                JSObject theJSForm = TeaVMMap.toJS(aScene.serialize());
                String theJSON = JSON.stringify(theJSForm);
                Blob theBlob = Blob.createJSONBlob(JSString.valueOf(theJSON));

                resourceAccessor.persistFile("/" + getIDForScene(aScene) + "/scene.json", theBlob,
                    new ResourceAccessor.CompleteCallback() {
                        @Override
                        public void fileWritten() {
                            aListener.written();
                        }

                        @Override
                        public void error(String aMessage) {
                            aListener.error(aMessage);
                        }
                });
            }

            @Override
            public void error(String aMessage) {
                aListener.error(aMessage);
            }
        });
    }

    public void load(LoadingListener aListener) {
        TeaVMGameLoader theGameLoader = resourceAccessor.createGameLoader(new TeaVMGameLoader.GameLoadedListener() {
            @Override
            public void onGameLoaded(Game aGame) {
                loadedGame = aGame;
                aListener.onGameLoaded(EditorState.this);
                loadScenes(aListener);
            }

            @Override
            public void onGameLoadedError(Throwable aError) {
                aListener.onGameLoadingError(aError);
            }
        });
        theGameLoader.loadFromServer();
    }

    private void loadScenes(LoadingListener aLoadingListener) {
        String[] theKnownScenes = loadedGame.getKnownScenes();
        for (String theScene : theKnownScenes) {
            TeaVMGameResourceLoader theLoader = resourceAccessor.createResourceLoaderFor(theScene);
            TeaVMGameSceneLoader theSceneLoader = resourceAccessor.createSceneLoader(
                    new TeaVMGameSceneLoader.GameSceneLoadedListener() {
                        @Override
                        public void onGameSceneLoaded(GameScene aScene) {
                            loadedScenes.put(theScene, aScene);
                            aLoadingListener.onSceneLoaded(EditorState.this, theScene);
                        }

                        @Override
                        public void onGameSceneLoadedError(Throwable aThrowable) {
                            aLoadingListener.onSceneLoadingError(EditorState.this, theScene, aThrowable);
                        }
                    }, runtimeFactory);
            theSceneLoader.loadFromServer(loadedGame, theScene, theLoader);
        }
    }

    public String[] getLoadedScenes() {
        List<String> theKeys = new ArrayList<>(loadedScenes.keySet());
        return theKeys.toArray(new String[theKeys.size()]);
    }
}