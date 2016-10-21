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

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorState {

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

    public EditorState(EditorProject aProject, AbstractGameRuntimeFactory aRuntimeFactory) {
        loadedScenes = new HashMap<>();
        editorProject = aProject;
        runtimeFactory = aRuntimeFactory;
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

    public void load(LoadingListener aListener) {
        TeaVMGameLoader theGameLoader = editorProject.createGameLoader(new TeaVMGameLoader.GameLoadedListener() {
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
            TeaVMGameResourceLoader theLoader = editorProject.createResourceLoaderFor(theScene);
            TeaVMGameSceneLoader theSceneLoader = editorProject.createSceneLoader(
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